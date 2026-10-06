package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final int PORT = 23456;

    public static void main(String[] args) {
        Database database = new Database();

        try(ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");

            while (!server.isClosed()) {
                Socket socket = server.accept();
                executor.submit(() -> {
                    boolean exit = database.handleClient(socket);
                    if(exit)
                        try {
                            server.close();
                        } catch (IOException e) {
                            System.out.println("Failed to stop server! " + e);
                        }
                });
            }
        } catch (IOException e) {
            System.out.println("Server error! " + e);
        } finally {
            executor.shutdown();
        }
    }
}
