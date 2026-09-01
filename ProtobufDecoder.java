import java.util.Base64;
import java.util.Arrays;
import java.util.HexFormat;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class ProtobufDecoder {
    public float protobufDecoder(String protobuf) {

        byte[] decodedBytes = Base64.getDecoder().decode(protobuf);

        int wireType = 0;
        int fieldNumber = 0;
        int dataLength = 0;
        String tickerName = "";

        float price = 0;
        long time = 0;

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
                time = 0;
                i += 6;
            }
            else {
            break;
            }
        }

        String hex = HexFormat.ofDelimiter(" ").formatHex(decodedBytes);

        String readableBytes = new String(decodedBytes, StandardCharsets.UTF_8);
        System.out.println("Ticker: " + tickerName + " | Price: " + price);
        return price;

    }
}
