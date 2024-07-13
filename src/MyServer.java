import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer {
    private static final int PORT = 4004;
    private static final Logger logger = Logger.getLogger(MyServer.class.getName());

    public static void main(String[] args) {
        MyServer server = new MyServer();
        try {
            server.serverStart();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Server error", e);
        }
    }

    public void serverStart() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server error", e);
            throw e;
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                String inputLine;
                while ((inputLine = input.readLine()) != null) {
                    logger.info("Received: " + inputLine);
                    output.write("Echo: " + inputLine +"\n");
                    output.flush();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Client connection error", e);
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error closing client socket", e);
                }
            }
        }
    }
}

