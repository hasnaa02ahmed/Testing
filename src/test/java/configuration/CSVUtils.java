package configuration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    public static Object[][] getTestData(String filePath) throws Exception {
        List<Object[]> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        br.readLine(); // Skip header

        while ((line = br.readLine()) != null) {
            // This regex splits by comma only if it's NOT inside double quotes
            String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            // Clean up quotes from the data
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].replace("\"", "").trim();
            }
            data.add(values);
        }

        br.close();
        return data.toArray(new Object[0][]);
    }
}