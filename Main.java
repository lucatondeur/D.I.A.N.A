import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Client client = new Client();
            System.out.print("What ticker would you like to track? ");
            String ticker = scanner.nextLine();
            System.out.print("What candle length would you like to observe? ");
            int candleLength = scanner.nextInt();
            client.connect(ticker, candleLength);
        } catch (Exception e) {
            System.out.println("Error: exiting program");
        }
    }
}
