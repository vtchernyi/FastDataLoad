package loader;

import model.AuxUtils;
import model.CountryLanguage;
import model.SyntheticKey;
import org.apache.ignite.binary.BinaryObject;
import org.h2.tools.Csv;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoadCountryLanguages extends AbstractLoadTask<BinaryObject> {
    @Override
    protected Map<Integer, Map<BinaryObject, BinaryObject>> getBinaries2bSent(String csvFileName) throws SQLException {
        int rowNumber = 0;
        Map<Integer, Map<BinaryObject, BinaryObject>> result = new HashMap<>();
        try (ResultSet rs = new Csv().read(csvFileName, null, null)) {
            while (rs.next()) {
                String countryCode = rs.getString("CountryCode");
                String language = rs.getString("Language");
                Boolean isOfficial = "'T'".equals(rs.getString("IsOfficial"));
                Double percentage = rs.getDouble("Percentage");
                rowNumber++;
                BinaryObject binKey = node.binary().toBinary(new SyntheticKey(countryCode, rowNumber));

                CountryLanguage countryLanguage = new CountryLanguage(language, isOfficial, percentage);
                BinaryObject binLanguage = node.binary().toBinary(countryLanguage);
                result.computeIfAbsent(affinity.partition(binLanguage), k -> new HashMap<>()).put(binKey, binLanguage);
            }
        }
        return result;
    }
}
