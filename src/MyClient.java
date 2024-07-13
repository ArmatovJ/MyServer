import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyClient {
    private static final String SERVER_ADDRESS = "localhost"; // Адрес сервера
    private static final int SERVER_PORT = 4004;              // Порт сервера
    private static final Logger logger = Logger.getLogger(MyClient.class.getName());

    public static void main(String[] args) {
        MyClient client = new MyClient();
        client.startClient();
    }

    public void startClient() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            String userInput;
            System.out.println("Enter message to send to server (type 'exit' to quit):");

            while (!(userInput = consoleInput.readLine()).equalsIgnoreCase("exit")) {
                // Отправляем сообщение на сервер
                output.write(userInput);
                output.newLine();
                output.flush();

                // Получаем ответ от сервера
                String serverResponse = input.readLine();
                System.out.println("Server response: " + serverResponse);

                System.out.println("Enter next message (or 'exit' to quit):");
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Client error", e);
        }
    }
}
