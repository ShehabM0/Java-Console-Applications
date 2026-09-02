package battleship;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try(Scanner sc = new Scanner(System.in)) {
            new Battleship(sc).launch();
        }
    }
}
