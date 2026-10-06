package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

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

    public static Map<String, String> loadDB(String path) {
        try {
            Path dbPath = Paths.get(path);
            Files.createDirectories(dbPath.getParent());

            Map<String, String> db = new HashMap<>();
            if(Files.exists(dbPath))
                try(Reader reader = Files.newBufferedReader(dbPath)) {
                    db = gson.fromJson(reader, Map.class);
                }

            return db;
        } catch (IOException e) {
            System.out.println("Failed to load database!" + e);
        }
        return null;
    }

    public static void saveDB(String path, Map<String, String> db) {
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
