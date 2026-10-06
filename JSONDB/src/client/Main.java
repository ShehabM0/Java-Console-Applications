package client;

import com.beust.jcommander.JCommander;

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
        String operation = args.getType().trim().toLowerCase();
        int idx = args.getIndex();
        String val = args.getMessage();

        try(
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");

            StringBuilder req = new StringBuilder(operation);
            if(!operation.equals("exit"))
                req.append(' ').append(idx);
            if(operation.equals("set"))
                req.append(' ').append(val);

            output.writeUTF(req.toString());
            System.out.printf("Sent: %s%n", req);

            String res = input.readUTF();
            System.out.printf("Received: %s%n", res);
        } catch (IOException e) {
             System.out.println("Client failed connecting to server! " + e);
        }
    }
}
