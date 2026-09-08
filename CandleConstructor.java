import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.Instant;
import java.util.Arrays;

public class CandleConstructor {
    public double[] candleConstructor(String[] data, double[] candleData, int candleLength) {

        if (candleData[5] < 1) {
            System.out.println("\n" + data[0] + " found...\n");
            candleData[5]++;
        }

        double currentPrice = Double.parseDouble(data[2]);
        long timeStamp = Long.parseLong(data[3]);

        ZonedDateTime dateTime = Instant.ofEpochMilli(timeStamp).atZone(ZoneOffset.UTC);
        int minute = dateTime.getMinute();

        double currentBucketId;
        if (candleLength == 1) {
            currentBucketId = Math.floor((timeStamp) / 60000);
        } else {
            int intervalStartMinute = (minute / candleLength) * candleLength;
            currentBucketId = dateTime.withMinute(intervalStartMinute).withSecond(0).withNano(0).toInstant().toEpochMilli();
        }

        if (candleData[0] == 0) {
            candleData[0] = currentPrice; // Open
            candleData[1] = currentPrice; // High
            candleData[2] = currentPrice; // Low
            candleData[3] = currentPrice; // Close
            candleData[4] = currentBucketId; // Current bucket id
            return candleData;
        }

        if (candleData[4] != currentBucketId) {

            printCandle(candleData, candleLength);
            System.out.println();

            candleData[0] = currentPrice;
            candleData[1] = currentPrice;
            candleData[2] = currentPrice;
            candleData[3] = currentPrice;
            candleData[4] = currentBucketId;
            return candleData;

        }

        if (currentPrice > candleData[1]) {
            candleData[1] = currentPrice;
        }

        if (currentPrice < candleData[2]) {
            candleData[2] = currentPrice;
        }

        candleData[3] = currentPrice;

        printCandle(candleData, candleLength);

        return candleData;
    }

    private void printCandle(double[] candleData, int candleLength) {

        long completedBucketMilli;

        if (candleLength == 1) {
            completedBucketMilli = (long) (candleData[4] * 60000);
        } else {
            completedBucketMilli = (long) (candleData[4]);
        }

        ZonedDateTime candleStart = Instant.ofEpochMilli(completedBucketMilli).atZone(ZoneOffset.UTC);
        ZonedDateTime candleEnd = candleStart.plusMinutes(candleLength);

        String timeString = String.format("%02d:%02d:%02d to %02d:%02d:%02d",
            candleStart.getHour(), candleStart.getMinute(), candleStart.getSecond(),
            candleEnd.getHour(), candleEnd.getMinute(), candleEnd.getSecond()
        );

        System.out.print("\033[1A\033[2K");
        System.out.flush();
        System.out.printf("[%dm | %s UTC] Open: %f | High: \033[32m%f\033[0m | Low: \033[31m%f\033[0m | Close: %f%n",
            candleLength,
            timeString,
            candleData[0],
            candleData[1],
            candleData[2],
            candleData[3]
        );
    }
}
