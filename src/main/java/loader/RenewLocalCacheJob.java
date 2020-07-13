package loader;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;

import java.util.Map;

public class RenewLocalCacheJob<KeyType> extends ComputeJobAdapter {
    private String localCacheName, jobLabel;
    private Map<KeyType, BinaryObject> addend;
    @IgniteInstanceResource
    private Ignite localNode;

    public RenewLocalCacheJob(String localCacheName, String jobLabel, Map<KeyType, BinaryObject> addend) {
        this.localCacheName = localCacheName;
        this.jobLabel = jobLabel;
        this.addend = addend;
    }

    @Override
    public Object execute() throws IgniteException {
        IgniteCache<KeyType, BinaryObject> targetCache = localNode.cache(localCacheName);
        targetCache.putAll(addend);
        System.out.println(jobLabel + " OK, addend size = " + addend.size());
        return addend.size();
    }
}
