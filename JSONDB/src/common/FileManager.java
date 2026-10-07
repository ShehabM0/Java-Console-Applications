package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class FileManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Request loadReq(String path) {
        try {
            Path dbPath = Paths.get(path);
            Files.createDirectories(dbPath.getParent());

            Request req = null;
            if(Files.exists(dbPath))
                try(Reader reader = Files.newBufferedReader(dbPath)) {
                    req = gson.fromJson(reader, Request.class);
                }

            return req;
        } catch (IOException e) {
            System.out.println("Failed to load request!" + e);
        }
        return null;
    }

    public static JsonObject loadDB(String path) {
        try {
            Path dbPath = Paths.get(path);
            Files.createDirectories(dbPath.getParent());

            JsonObject db = new JsonObject();
            if(Files.exists(dbPath))
                try(Reader reader = Files.newBufferedReader(dbPath)) {
                    db = gson.fromJson(reader, JsonObject.class);
                }

            return db;
        } catch (IOException e) {
            System.out.println("Failed to load database!" + e);
        }
        return null;
    }

    public static void saveDB(String path, JsonObject db) {
        try {
            Path dbPath = Paths.get(path);
            Files.createDirectories(dbPath.getParent());

            try(Writer writer = Files.newBufferedWriter(dbPath)) {
                gson.toJson(db, writer);
            }
        } catch (IOException e) {
            System.out.println("Failed to save database " + e);
        }
    }
}
