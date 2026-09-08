import java.util.Base64;
import java.util.Arrays;
import java.util.HexFormat;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class ProtobufDecoder {
    public String[] protobufDecoder(String protobuf) {
        //System.out.println(protobuf);

        byte[] decodedBytes = Base64.getDecoder().decode(protobuf);

        int wireType = 0;
        int fieldNumber = 0;
        int dataLength = 0;
        String tickerName = "";

        double price = 0;
        String formattedDate = "";
        long epochTime = 0;

        for (int i = 0; i < decodedBytes.length; ) {
            int tag = decodedBytes[i] & 0xFF;
            wireType = tag & 7;
            fieldNumber = tag >> 3;

            if ((wireType == 2) && (fieldNumber == 1)) {
                dataLength = decodedBytes[i+1] & 0xFF;
                tickerName = new String(Arrays.copyOfRange(decodedBytes, i+2, i+2+dataLength), StandardCharsets.UTF_8);
                i += 2 + dataLength;
            }
            else if ((wireType == 5) && (fieldNumber == 2)) {
                price = ByteBuffer.wrap(Arrays.copyOfRange(decodedBytes, i+1, i+5)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                i += 5;
                //break;
            }
            else if ((wireType == 0) && (fieldNumber == 3)) {
                int start = i + 1;
                long varintResult = 0;
                int shift = 0;

                while (start < decodedBytes.length) {

                    byte b = decodedBytes[start];
                    varintResult = varintResult | (long) (b & 0x7F) << shift;
                    start++;

                    if ((b & 0x80) == 0) {
                    break;
                    }
                    shift += 7;
                }
                long epochTimeUnsigned = varintResult;
                epochTime = epochTimeUnsigned / 2;

                Instant instant = Instant.ofEpochSecond(epochTime / 1000);
                LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss"));

                i = start;
            }
            else {
            break;
            }
        }

        String hex = HexFormat.ofDelimiter(" ").formatHex(decodedBytes);

        String readableBytes = new String(decodedBytes, StandardCharsets.UTF_8);
        //System.out.println("Ticker: " + tickerName + " | Time: " + formattedDate + " | Price: " + price);

        String[] data = {tickerName, formattedDate, price + "", epochTime + ""};
        return data;

    }
}
