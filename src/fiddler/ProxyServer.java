package fiddler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class ProxyServer implements Runnable {
    private Socket client;
    private Socket server;
    private Thread thread;

    private static int BUFFER_SIZE = 8192;
    private static int THREAD_COUNT = 0;
    private static String CRLF = "\r\n";

    ProxyServer(Socket client) {
        this.client = client;
        thread = new Thread(this, "Thread " + THREAD_COUNT);
        THREAD_COUNT++;
        thread.run();
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            BufferedOutputStream serverOutputStream = null;
            BufferedOutputStream clientOutputStream = null;
            BufferedInputStream serverInputStream = null;

            // Get request from client
            BufferedInputStream clientInputStream = new BufferedInputStream(client.getInputStream());
            int clientBytes = clientInputStream.read(buffer);

            if (clientBytes > 0) {
                System.out.print("\nBytes from client: " + clientBytes);

                // Get host from request and send request to real server
                String clientRequest = new String(buffer);
                server = new Socket(getHostFromRequest(clientRequest), 80);
                serverOutputStream = new BufferedOutputStream(server.getOutputStream());
                serverOutputStream.write(clientRequest.getBytes());
                serverOutputStream.flush();

                // Write response from server to client
                clientOutputStream = new BufferedOutputStream(client.getOutputStream());
                serverInputStream = new BufferedInputStream(server.getInputStream());
                buffer = new byte[BUFFER_SIZE];
                int buffSize;
                int serverBytes = 0;
                while ((buffSize = serverInputStream.read(buffer)) != -1) {
                    clientOutputStream.write(buffer, 0, buffSize);
                    serverBytes += buffSize;
                }
                System.out.print("\nBytes from server: " + serverBytes);
                clientOutputStream.flush();
            }

            // Close connections
            clientInputStream.close();
            if (clientOutputStream != null) {
                clientOutputStream.close();
            }
            if (serverInputStream != null) {
                serverInputStream.close();
            }
            if (serverOutputStream != null) {
                serverOutputStream.close();
            }
        } catch (Exception e) {
            System.out.print("\nError running thread");
        }
    }

    private String getHostFromRequest(String request) {
        StringTokenizer tok = new StringTokenizer(request, CRLF);
        // Discard the first token which is the method
        tok.nextToken();
        return tok.nextToken().substring("Host: ".length());
    }
}
