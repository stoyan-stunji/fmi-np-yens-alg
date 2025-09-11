import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1968;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)
        ) {
            scanner.useDelimiter("\n");
            String firstLine = promptUser(scanner, "Enter NUM_OF_EDGES, START, TARGET, K (space-separated): ");
            sendLine(out, firstLine);
            String[] inputParts = firstLine.trim().split("\\s+");
            if (inputParts.length < 4) {
                System.err.println("Invalid input format. Expected: NUM_OF_EDGES START TARGET K");
                return;
            }

            int edgeCount = Integer.parseInt(inputParts[0]);
            System.out.println("Enter each edge in the format: FROM TO WEIGHT");
            for (int i = 0; i < edgeCount; i++) {
                String edgeInput = promptUser(scanner, "Edge " + (i + 1) + ": ");
                sendLine(out, edgeInput);
            }

            String response = in.readLine();
            System.out.println("\nResult from server:");
            System.out.println(response);
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String promptUser(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void sendLine(PrintWriter out, String line) {
        out.println(line);
        out.flush();
    }
}
