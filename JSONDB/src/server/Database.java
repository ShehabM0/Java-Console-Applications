package server;

import com.google.gson.Gson;
import common.FileManager;
import common.Request;
import common.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Database {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final String DB_PATH = System.getProperty("user.dir") + "/src/server/data/" + "db.json";
    private final Map<String, String> db;

    Database() {
        db = FileManager.loadDB(DB_PATH);
    }

    boolean handleClient(Socket socket) {
        try (
                socket;
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
                return false;
            }

            res = switch (operation) {
                case SET -> set(key, val);
                case GET -> get(key);
                case DELETE -> delete(key);
                case EXIT -> new Response("OK");
            };
            output.writeUTF(new Gson().toJson(res));

            return operation == Operation.EXIT;
        } catch (IOException e) {
            System.out.println("Server error! " + e);
            return false;
        }
    }

    private Response set(String key, String val) {
        lock.writeLock().lock();
        try {
            db.put(key, val);
            FileManager.saveDB(DB_PATH, db);
            return new Response("OK");
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Response get(String key) {
        lock.readLock().lock();
        try {
            if (!db.containsKey(key))
                return new Response(
                        "ERROR",
                        null,
                        "No such key"
                );

            return new Response("OK", db.get(key));
        } finally {
            lock.readLock().unlock();
        }
    }

    private Response delete(String key) {
        lock.writeLock().lock();
        try {
            if (!db.containsKey(key))
                return new Response(
                        "ERROR",
                        null,
                        "No such key"
                );

            db.remove(key);
            FileManager.saveDB(DB_PATH, db);
            return new Response("OK");
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Operation parseOperation(String operation) {
        try {
            return Operation.valueOf(operation.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
