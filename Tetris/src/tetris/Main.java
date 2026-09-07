package tetris;

import java.util.Scanner;

class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int m = sc.nextInt(), n = sc.nextInt();
        Board board= new Board(n);
        board.display();

        String action = sc.next();
        while(!action.equalsIgnoreCase("exit")) {
            if(action.equalsIgnoreCase("piece")) {
                Tetromino tetromino = getTetrominoInput();
                board.spawn(tetromino);
                board.display();
                action = sc.next();
                continue;
            }

            try {
                Move move = Move.valueOf(action.toUpperCase());
                if(move != Move.DOWN) {
                    board.moveTetrominoDown();
                    if(board.isGameOver()) {
                        board.display();
                        System.out.println("Game Over!");
                        break;
                    }
                }
                switch (move) {
                    case ROTATE -> board.rotateTetromino();
                    case RIGHT -> board.moveTetrominoRight();
                    case LEFT -> board.moveTetrominoLeft();
                    case DOWN -> board.moveTetrominoDown();
                    case BREAK -> board.disappear();
                }
                if(move == Move.DOWN && board.isGameOver()) {
                    board.display();
                    System.out.println("Game Over!");
                    break;
                }
                board.display();
            } catch (IllegalArgumentException _) {
                System.out.println("Unknown move: " + action);
            }
            action = sc.next();
        }
    }

    private static Tetromino getTetrominoInput() {
        String tetrominoInput = sc.next();
        Tetromino tetromino = null;
        while (tetromino == null) {
            try {
                tetromino = switch (Tetromino.valueOf(tetrominoInput.toUpperCase())) {
                    case O -> Tetromino.O;
                    case I -> Tetromino.I;
                    case S -> Tetromino.S;
                    case Z -> Tetromino.Z;
                    case L -> Tetromino.L;
                    case J -> Tetromino.J;
                    case T -> Tetromino.T;
                };
            } catch (IllegalArgumentException _) {
                System.out.println("Unknown tetromino: " + tetrominoInput);
                tetrominoInput = sc.next();
            }
        }
        return tetromino;
    }
}
