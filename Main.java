import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Client client = new Client();
            System.out.print("What ticker would you like to track? ");
            String ticker = scanner.nextLine();
            client.connect(ticker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
