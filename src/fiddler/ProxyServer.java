package fiddler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ProxyServer implements Runnable {
    private Socket client;
    private Socket server;
    private Thread thread;

    private static int BUFFER_SIZE = 8192;
    private static int THREAD_COUNT = 0;
    private static String CRLF = "\r\n";
    private static String FILENAME = "fiddler.txt";

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
            Path file = Paths.get(FILENAME);

            // Get request from client
            BufferedInputStream clientInputStream = new BufferedInputStream(client.getInputStream());
            int clientBytes = clientInputStream.read(buffer);

            // Get host from request and send request to real server
            String clientRequest = new String(buffer);
            server = new Socket(getHostFromRequest(clientRequest), 80);
            BufferedOutputStream serverOutputStream = new BufferedOutputStream(server.getOutputStream());
            serverOutputStream.write(clientRequest.getBytes());
            serverOutputStream.flush();

            // Write response from server to client
            BufferedOutputStream clientOutputStream = new BufferedOutputStream(client.getOutputStream());
            BufferedInputStream serverInputStream = new BufferedInputStream(server.getInputStream());
            buffer = new byte[BUFFER_SIZE];
            int buffsize = 0;
            int internetBytes = 0;
            while ((buffsize = serverInputStream.read(buffer)) != -1) {
                clientOutputStream.write(buffer, 0, buffsize);
                internetBytes += buffsize;
            }

            // Write server bytes into the file
            Files.write(file, Arrays.asList("Bytes from client " + clientBytes,
                    "Bytes from server " + internetBytes),
                    Charset.forName("UTF-8"),
                    StandardOpenOption.APPEND);

            // Close connections
            clientOutputStream.flush();
            clientInputStream.close();
            clientOutputStream.close();
            serverInputStream.close();
            serverOutputStream.close();
        } catch (Exception e) {
            System.out.print("Error running thread");
        }
    }

    private String getHostFromRequest(String request) {
        StringTokenizer tok = new StringTokenizer(request, CRLF);
        // Discard the first token which is the method
        tok.nextToken();
        return tok.nextToken().substring("Host: ".length());
    }
}
