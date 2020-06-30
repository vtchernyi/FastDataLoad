import org.h2.tools.Csv;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class LoadApp {
    public static void main(String[] args) throws SQLException, UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "utf-8"));
        System.setErr(new PrintStream(System.err, true, "utf-8"));

        printTopLines("sampleData/city.csv", 3);
        printTopLines("sampleData/country.csv", 3);
        printTopLines("sampleData/countryLanguage.csv", 3);

        System.out.println("processing complete");
    }

    private static void printTopLines(String fileName, int maxRows) throws SQLException {
        ResultSet rs = new Csv().read(fileName, null, null);
        System.out.println(" ===== " + fileName + " ===== ");
        ResultSetMetaData meta = rs.getMetaData();
        int rowCount = 0;
        while (rs.next()) {
            if (++rowCount > maxRows) continue;
            for (int i = 0; i < meta.getColumnCount(); i++) {
                System.out.print(
                        meta.getColumnLabel(i + 1) + ": " +
                                rs.getString(meta.getColumnLabel(i + 1))+"; ");
            }
            System.out.println();
        }
        System.out.println("total rows: "+rowCount);
        rs.close();

    }
}
