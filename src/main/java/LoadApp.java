import loader.LoadCountries;
import model.LoaderAgrument;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class LoadApp {
    public static Ignite ignite;

    public static void main(String[] args) throws SQLException, UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "utf-8"));
        System.setErr(new PrintStream(System.err, true, "utf-8"));
        try {
            ignite = Ignition.start("clientNodeConfig.xml");
            IgniteCache<String, BinaryObject> countryCache = ignite.cache("countryCache").withKeepBinary();

            LoaderAgrument loadArg = new LoaderAgrument();
            loadArg.csvFileName = "sampleData/country.csv";
            loadArg.targetCacheName = "countryCache";
            ignite.compute().execute(LoadCountries.class,ignite.binary().toBinary(loadArg));

            System.out.println("'AFG' --> " + countryCache.get("'AFG'"));
            System.out.println("processing complete");
        } finally {
            Ignition.stop(true);
        }
    }
}
