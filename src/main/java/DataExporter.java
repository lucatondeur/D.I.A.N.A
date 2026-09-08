import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataExporter {
    public void dataExporter(String fileName, String candleData) {
        File file = new File(fileName);

        boolean fileExists = file.exists() && file.length() > 0;

        try (FileWriter writer = new FileWriter(file, true)) {

            if (!fileExists) {
                writer.append("Candle interval, Time, Open, High, Low, Close\n");
            }

            writer.append(candleData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
