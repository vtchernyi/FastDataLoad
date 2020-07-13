package loader;

import model.AuxUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// load data from csv file
public abstract class AbstractLoadTask<TargetCacheKeyType> extends ComputeTaskAdapter<BinaryObject, Integer> {
    @IgniteInstanceResource
    protected Ignite node;
    protected long startTime;
    protected int rowCount;
    protected Affinity affinity;
    protected String targetCacheName, csvFileName;

    @Override
    public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> list, BinaryObject params) throws IgniteException {
        startTime = System.currentTimeMillis();
        targetCacheName = params.field("targetCacheName");
        csvFileName = params.field("csvFileName");  // in real world should be replaced with connURL and sqlQuery fields
        affinity = node.affinity(targetCacheName);
        Map<ComputeJobAdapter, ClusterNode> jobs = new HashMap<>();

        int totalPartitions = affinity.partitions();
        Map<Integer, Map<TargetCacheKeyType, BinaryObject>> binaries2bSent = null;
        try {
            binaries2bSent = getBinaries2bSent(csvFileName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (int partNo : binaries2bSent.keySet()) {
            jobs.put(new RenewLocalCacheJob(targetCacheName,
                            targetCacheName + " " + partNo + "/" + totalPartitions, binaries2bSent.get(partNo)),
                    affinity.mapPartitionToNode(partNo));
        }
        return jobs;
    }

    protected abstract Map<Integer, Map<TargetCacheKeyType, BinaryObject>> getBinaries2bSent(String csvFileName) throws SQLException;

    @Override
    public Integer reduce(List<ComputeJobResult> list) throws IgniteException {
        int totalRows = 0;
        startTime = System.currentTimeMillis();
        for (ComputeJobResult result : list) {
            totalRows += result.<Integer>getData();
        }
        AuxUtils.printElapsedTime(this.getClass().getSimpleName() + " reduce: " + totalRows, startTime);
        return totalRows;
    }
}





