import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class Client {
    private WebSocket webSocket;

    public void connect(String ticker) throws InterruptedException {

        double open = 0;
        double high = 0;
        double low = Integer.MAX_VALUE;
        double close = 0;
        double currentIntervalWindowId = 0;
        double counter = 0;

        double[] candleData = {open, high, low, close, currentIntervalWindowId, counter};

        String url = "wss://streamer.finance.yahoo.com";
        HttpClient httpClient = HttpClient.newHttpClient();
        webSocket = httpClient.newWebSocketBuilder().header("User-Agent", "Mozilla/5.0").buildAsync(URI.create(url), new WebSocket.Listener() {

            @Override
            public void onOpen(WebSocket webSocket) {
                webSocket.request(1);
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                //System.out.println(data);

                ProtobufDecoder protobuf = new ProtobufDecoder();

                CandleConstructor candles = new CandleConstructor();
                candles.candleConstructor(protobuf.protobufDecoder(data.toString()), candleData, 1);

                webSocket.request(1);
                return null;
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                error.printStackTrace();
            }

        } ).join();

        String message = """
            {"subscribe":["%s"]}
            """.formatted(ticker);

        webSocket.sendText(message, true);


        while (true) {
            Thread.sleep(1000);
        }
    }
}
