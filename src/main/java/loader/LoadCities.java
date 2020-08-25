package loader;

import model.AuxUtils;
import model.City;
import model.SyntheticKey;
import org.apache.ignite.binary.BinaryObject;
import org.h2.tools.Csv;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoadCities extends AbstractLoadTask<BinaryObject> {
    @Override
    protected Map<Integer, Map<BinaryObject, BinaryObject>> getBinaries2bSent(String csvFileName) throws SQLException {
        int rowCount = 0;
        Map<Integer, Map<BinaryObject, BinaryObject>> result = new HashMap<>();
        try (ResultSet rs = new Csv().read(csvFileName, null, null)) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                String countryCode = rs.getString("CountryCode");
                String district = rs.getString("District");
                int population = rs.getInt("Population");

                BinaryObject binKey = node.binary().toBinary(new SyntheticKey(countryCode, id));
                City city = new City(name, district, population);
                BinaryObject binCity = node.binary().toBinary(city);
                result.computeIfAbsent(affinity.partition(binKey), k -> new HashMap<>()).put(binKey, binCity);
                rowCount++;
            }
        }
        return result;
    }
}
