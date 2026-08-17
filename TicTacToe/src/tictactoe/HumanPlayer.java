package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;

class HumanPlayer implements Player {
    private final Scanner scanner = new Scanner(System.in);

    private final int GRID_SIZE;
    private final CellType[][] grid;

    HumanPlayer(CellType[][] grid) {
        this.grid = grid;
        GRID_SIZE = grid.length;
    }

    @Override
    public Cell makeMove() {
        Cell pickedCell = null;
        do {
            try {
                System.out.print("Enter the coordinates: ");
                int row = scanner.nextInt() - 1, col = scanner.nextInt() - 1;

                if(row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
                    System.out.printf("Coordinates should be from 1 to %d!%n", GRID_SIZE);
                }
                else if(grid[row][col] != CellType.EMPTY) {
                    System.out.println("This cell is occupied! Choose another one!");
                } else {
                    pickedCell = new Cell(row, col);
                }
            } catch (InputMismatchException e) {
                System.out.println("You should enter numbers!");
                scanner.nextLine();
            }
        } while (pickedCell == null);

        return pickedCell;
    }
}
