package loader;

import model.AuxUtils;
import model.Country;
import org.apache.ignite.binary.BinaryObject;
import org.h2.tools.Csv;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoadCountries extends AbstractLoadTask<String> {
    @Override
    protected Map<Integer, Map<String, BinaryObject>> getBinaries2bSent(String csvFileName) throws SQLException {
        int rowCount = 0;
        Map<Integer, Map<String, BinaryObject>> result = new HashMap<>();
        try (ResultSet rs = new Csv().read(csvFileName, null, null)) {
            while (rs.next()) {
                String code = rs.getString("Code");
                String name = rs.getString("Name");
                String continent = rs.getString("Continent");
                String region = rs.getString("Region");
                Double surfaceArea = rs.getDouble("SurfaceArea");
                Integer indepYear = rs.getInt("IndepYear");
                Integer population = rs.getInt("Population");
                Double lifeExpectancy = rs.getDouble("LifeExpectancy");
                Double gNP = rs.getDouble("GNP");
                Double gNPOld = rs.getDouble("GNPOld");
                String localName = rs.getString("LocalName");
                String governmentForm = rs.getString("GovernmentForm");
                String headOfState = rs.getString("HeadOfState");
                Integer capitalId = rs.getInt("Capital");
                String сode2 = rs.getString("Code2");

                Country country = new Country(code, name, continent, region, localName, governmentForm, headOfState, сode2,
                        surfaceArea, lifeExpectancy, gNP, gNPOld, indepYear, population, capitalId);
                BinaryObject binCountry = node.binary().toBinary(country);
                result.computeIfAbsent(affinity.partition(code), k -> new HashMap<>()).put(code, binCountry);
                rowCount++;
            }
            AuxUtils.printElapsedTime("total rows: " + rowCount, startTime);
        }
        return result;
    }
}
