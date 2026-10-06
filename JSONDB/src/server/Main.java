package server;

import client.Request;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final int PORT = 23456;
    private static final Map<String, String> db = new HashMap<>();

    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            boolean running = true;

            while (running) {
                try (
                        Socket socket = server.accept();
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String json = input.readUTF();

                    Request req = new Gson().fromJson(json, Request.class);
                    Operation operation = parseOperation(req.getType());
                    String key = req.getKey(), val = req.getValue();

                    Response res;
                    if (operation == null) {
                        res = new Response(
                                "ERROR",
                                null,
                                "Enter valid operation type!"
                        );
                        output.writeUTF(new Gson().toJson(res));
                        continue;
                    }

                    res = switch (operation) {
                        case SET -> set(key, val);
                        case GET -> get(key);
                        case DELETE -> delete(key);
                        case EXIT -> new Response("OK");
                    };
                    output.writeUTF(new Gson().toJson(res));

                    if (operation == Operation.EXIT)
                        running = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Server error! " + e);
        }
    }

    static Response set(String key, String val) {
        db.put(key, val);
        return new Response("OK");
    }

    static Response get(String key) {
        if (!db.containsKey(key))
            return new Response(
                    "ERROR",
                    null,
                    "No such key"
            );

        return new Response("OK", db.get(key));
    }

    static Response delete(String key) {
        if (!db.containsKey(key))
            return new Response(
                    "ERROR",
                    null,
                    "No such key"
            );

        db.remove(key);
        return new Response("OK");
    }

    static Operation parseOperation(String operation) {
        try {
            return Operation.valueOf(operation.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
