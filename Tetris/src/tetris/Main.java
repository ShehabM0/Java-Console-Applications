package tetris;

import java.util.Scanner;
import java.util.Random;

class Main {
    private static final Tetromino[] tetrominoList = Tetromino.values();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int n = sc.nextInt();
        Board board= new Board(n);
        board.display();

        Tetromino tetromino;
        String action;
        while (true) {
            tetromino = tetrominoList[new Random().nextInt(tetrominoList.length)];
            if(!board.spawn(tetromino)) {
                System.out.println("Game Over!");
                break;
            }

            board.display();
            action = sc.next();
            while(!action.equalsIgnoreCase("exit") &&
                    board.canMoveDown()) {
                try {
                    Move move = Move.valueOf(action.toUpperCase());
                    switch (move) {
                        case ROTATE -> board.rotateTetromino();
                        case RIGHT -> board.moveTetrominoRight();
                        case LEFT -> board.moveTetrominoLeft();
                        case DOWN -> board.moveTetrominoDown();
                    }
                    board.moveTetrominoDown();
                    board.disappear();
                    board.display();
                } catch (IllegalArgumentException _) {
                    System.out.println("Unknown move: " + action);
                }

                action = sc.next();
            }
        }
    }
}
