package server;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final int N = 1000;
    private static final String[] db = new String[N];
    private static final Scanner sc = new Scanner(System.in);

    static {
        Arrays.fill(db, "");
    }

    public static void main(String[] args) {
        Operation operation = getOperationInput();
        while (operation != Operation.EXIT) {
            switch (operation) {
                case SET -> {
                    int idx = getIndexInput() - 1;
                    String val = sc.nextLine().trim();
                    set(idx, val);
                }
                case GET -> {
                    int idx = getIndexInput() - 1;
                    get(idx);
                }
                case DELETE -> {
                    int idx = getIndexInput() - 1;
                    delete(idx);
                }
            }
            operation = getOperationInput();
        }
    }

    static void set(int idx, String val) {
        if(!validateIdx(idx)) {
            System.out.println("ERROR");
            return;
        }
        db[idx] = val;
        System.out.println("OK");
    }

    static void get(int idx) {
        if(!validateIdx(idx) || db[idx].isBlank()) {
            System.out.println("ERROR");
            return;
        }

        System.out.println(db[idx]);
    }

    static void delete(int idx) {
        if(!validateIdx(idx)) {
            System.out.println("ERROR");
            return;
        }

        db[idx] = "";
        System.out.println("OK");
    }

    static boolean validateIdx(int idx) {
        return idx > -1 && idx < N;
    }

    static int getIndexInput() {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter valid index!");
            }
        }
    }

    static Operation getOperationInput() {
        String operationStr = getStrInput();
        while (true) {
            try {
                return Operation.valueOf(operationStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Enter valid operation!");
                operationStr = getStrInput();
            }
        }
    }

    static String getStrInput() {
        String in = sc.next();
        while (in.isBlank()) {
            System.out.println("Enter valid string!");
            in = sc.next();
        }
        return in;
    }
}
