package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.FileManager;
import common.Request;
import common.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Database {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final String DB_PATH = System.getProperty("user.dir") + "/src/server/data/" + "db.json";
    private final JsonObject db;

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
            Object key = req.getKey();
            JsonElement val = req.getValue();

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
            output.writeUTF(gson.toJson(res));

            return operation == Operation.EXIT;
        } catch (IOException e) {
            System.out.println("Server error! " + e);
            return false;
        }
    }

    private Response set(Object key, JsonElement val) {
        lock.writeLock().lock();
        try {
            if (key instanceof String keyStr) {
                db.add(keyStr, val);
            } else { // Array
                ArrayList<String> keys = (ArrayList<String>) key;
                int n = keys.size();

                JsonElement curr = db;
                for (int i = 0; i < n - 1; i++) {
                    JsonObject obj = curr.getAsJsonObject();
                    if (!obj.has(keys.get(i)))
                        obj.add(keys.get(i), new JsonObject());
                    curr = obj.get(keys.get(i));
                }
                curr.getAsJsonObject().add(keys.get(n-1), val);
            }
            FileManager.saveDB(DB_PATH, db);
            return new Response("OK");
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Response get(Object key) {
        lock.readLock().lock();
        try {
            if (key instanceof String keyStr) {
                if (!db.has(keyStr))
                    return new Response("ERROR", null, "No such key");
                return new Response("OK", db.get(keyStr));
            } else { // Array
                ArrayList<String> keys = (ArrayList<String>) key;

                JsonElement curr = db;
                for (String ikey : keys) {
                    if (!curr.isJsonObject())
                        return new Response("ERROR", null, "No such key");
                    JsonObject obj = curr.getAsJsonObject();
                    if (!obj.has(ikey))
                        return new Response("ERROR", null, "No such key");
                    curr = obj.get(ikey);
                }
                return new Response("OK", curr);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    private Response delete(Object key) {
        lock.writeLock().lock();
        try {
            if (key instanceof String keyStr) {
                if (!db.has(keyStr))
                    return new Response("ERROR", null, "No such key");
                db.remove(keyStr);
            } else { // Array
                ArrayList<String> keys = (ArrayList<String>) key;
                int n = keys.size();

                JsonElement curr = db;
                for (int i = 0; i < n - 1; i++) {
                    if (!curr.isJsonObject())
                        return new Response("ERROR", null, "No such key");
                    JsonObject obj = curr.getAsJsonObject();
                    if (!obj.has(keys.get(i)))
                        return new Response("ERROR", null, "No such key");
                    curr = obj.get(keys.get(i));
                }
                curr.getAsJsonObject().remove(keys.get(n - 1));
            }
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
