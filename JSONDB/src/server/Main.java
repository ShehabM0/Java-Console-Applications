package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Main {
    private static final int PORT = 23456;
    private static final int N = 1000;
    private static final String[] db = new String[N];

    static {
        Arrays.fill(db, "");
    }

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
                    String req = input.readUTF();

                    String[] reqBody = split(req);
                    Operation operation = parseOperation(reqBody[0]);
                    Integer idx = null;
                    String val = null;

                    if (operation == null) {
                        output.writeUTF("ERROR");
                        continue;
                    }
                    if (operation != Operation.EXIT) {
                        idx = parseIndex(reqBody[1]) - 1;
                        if(!validateIdx(idx)) {
                            output.writeUTF("ERROR");
                            continue;
                        }
                    }
                    if(operation == Operation.SET)
                        val = reqBody[2];

                    String res = switch (operation) {
                        case SET -> set(idx, val);
                        case GET -> get(idx);
                        case DELETE -> delete(idx);
                        case EXIT -> "OK";
                    };
                    output.writeUTF(res);

                    if (operation == Operation.EXIT)
                        running = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Server error! " + e);
        }
    }

    static String[] split(String s) {
        int n = s.length();
        StringBuilder str = new StringBuilder();
        String[] res = new String[3];
        int k = 0;
        for(int i = 0; i < n; i++)
            if(k < 2 && s.charAt(i) == ' ') {
                res[k++] = str.toString();
                str.setLength(0);
            } else {
                str.append(s.charAt(i));
            }
        res[k] = str.toString();
        return res;
    }

    static String set(int idx, String val) {
        if(!validateIdx(idx))
            return "ERROR";

        db[idx] = val;
        return "OK";
    }

    static String get(int idx) {
        if(!validateIdx(idx) || db[idx].isBlank())
            return "ERROR";

        return db[idx];
    }

    static String delete(int idx) {
        if(!validateIdx(idx))
            return "ERROR";

        db[idx] = "";
        return "OK";
    }

    static boolean validateIdx(int idx) {
        return idx > -1 && idx < N;
    }

    static int parseIndex(String index) {
        try {
            return Integer.parseInt(index);
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    static Operation parseOperation(String operation) {
        try {
            return Operation.valueOf(operation.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
