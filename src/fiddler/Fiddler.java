package fiddler;

import java.net.ServerSocket;
import java.net.Socket;

public class Fiddler {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8080);
            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    new ProxyServer(client);
                } catch (Exception e) {
                    System.out.print("Something was wrong " + e);
                }
            }
        } catch (Exception e) {
            System.out.print("Error opening socket");
        }
    }

}