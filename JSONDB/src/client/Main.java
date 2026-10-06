package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import common.FileManager;
import common.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] argv) {
        Args args = new Args();
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        try(
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            String inputFile = args.getInputFile();

            Request req;
            if(inputFile != null) {
                String FILE_PATH = System.getProperty("user.dir") + "/src/client/data/" + inputFile;
                req = FileManager.loadReq(FILE_PATH);
            } else {
                String type = args.getType().trim().toLowerCase();
                String key = args.getKey(), val = args.getValue();
                if(type.equals("exit"))
                    req = new Request(type);
                else if(type.equals("set"))
                    req = new Request(type, key, val);
                else
                    req = new Request(type, key);
            }

            String json = new Gson().toJson(req);
            output.writeUTF(json);
            System.out.printf("Sent: %s%n", json);

            String res = input.readUTF();
            System.out.printf("Received: %s%n", res);
        } catch (IOException e) {
             System.out.println("Client failed connecting to server! " + e);
        }
    }
}
