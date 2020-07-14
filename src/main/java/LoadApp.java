import loader.LoadCities;
import loader.LoadCountries;
import loader.LoadCountryLanguages;
import model.LoaderAgrument;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.internal.binary.BinaryObjectImpl;

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

            LoaderAgrument countryArg = new LoaderAgrument();
            countryArg.csvFileName = "sampleData/country.csv";
            countryArg.targetCacheName = "countryCache";
            BinaryObject binCountryArg=ignite.binary().toBinary(countryArg);
            ignite.compute().execute(LoadCountries.class,binCountryArg);

            LoaderAgrument cityArg = new LoaderAgrument();
            cityArg.csvFileName = "sampleData/city.csv";
            cityArg.targetCacheName = "cityCache";
            BinaryObject binCityArg=ignite.binary().toBinary(cityArg);
            ignite.compute().execute(LoadCities.class,binCityArg);

            LoaderAgrument languageArg = new LoaderAgrument();
            languageArg.csvFileName = "sampleData/countryLanguage.csv";
            languageArg.targetCacheName = "countryLanguageCache";
            BinaryObject binLanguageArg=ignite.binary().toBinary(languageArg);
            ignite.compute().execute(LoadCountryLanguages.class,binLanguageArg);
            System.out.println("processing complete");
        } finally {
            Ignition.stop(true);
        }
    }
}
