package configuration;
import java.io.*;
import java.util.*;

public class CSVUtils {

    public static Object[][] getTestData(String filePath) throws Exception {
        List<Object[]> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        br.readLine();

        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            data.add(values);
        }

        br.close();
        return data.toArray(new Object[0][]);
    }
}

