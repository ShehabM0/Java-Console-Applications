package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;

class HumanPlayer implements Player {
    private final Scanner scanner;
    private final Grid grid;
    private final int gridSize;

    HumanPlayer(Grid grid, Scanner scanner) {
        this.scanner = scanner;
        this.grid = grid;
        this.gridSize = grid.getSize();
    }

    @Override
    public Cell makeMove() {
        Cell pickedCell = null;
        do {
            try {
                System.out.print("Enter the coordinates: ");
                int row = scanner.nextInt() - 1, col = scanner.nextInt() - 1;

                if(row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
                    System.out.printf("Coordinates should be from 1 to %d!%n", gridSize);
                }
                else if(grid.getCellType(row, col) != CellType.EMPTY) {
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
