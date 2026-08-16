package tictactoe;

import java.util.*;

class TicTacToe {
    private final Scanner scanner = new Scanner(System.in);
    private final int GRID_SIZE = 3;

    private final CellType[][] grid = new CellType[GRID_SIZE][GRID_SIZE];

    private GridState gridState = GridState.NOT_FINISHED;
    int xCells, oCells;

    TicTacToe() {
        for (int row = 0; row < GRID_SIZE; row++)
            for (int col = 0; col < GRID_SIZE; col++)
                grid[row][col] = CellType.EMPTY;
        printGrid();

        while(gridState == GridState.NOT_FINISHED) {
            userTurn();
            printGrid();
            botTurn();
            printGrid();
        }

        System.out.println(gridState.state());
    }

    void userTurn() {
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

        addCell(pickedCell, CellType.X);
    }

    void botTurn() {
        System.out.println("Making move level \"easy\"");
        List<Cell> availableCells = new ArrayList<>();
        for (int row = 0; row < GRID_SIZE; row++)
            for (int col = 0; col < GRID_SIZE; col++)
                if (grid[row][col] == CellType.EMPTY)
                    availableCells.add(new Cell(row, col));

        int availableCellsLen = availableCells.size();
        int idx = new Random().nextInt(availableCellsLen);
        Cell pickedCell = availableCells.get(idx);

        addCell(pickedCell, CellType.O);
    }

    void updateGridState() {
        boolean xWins = false, oWins = false;
        if(scanGrid(CellType.X))
            xWins = true;
        if(scanGrid(CellType.O))
            oWins = true;

        if(xWins) {
            gridState = GridState.X;
        } else if(oWins) {
            gridState = GridState.O;
        } else if(xCells + oCells == GRID_SIZE * GRID_SIZE) {
            gridState = GridState.DRAW;
        }
    }

    boolean scanGrid(CellType cellType) {
        int i, j;
        // row, col
        for(i = 0; i < GRID_SIZE; i++) {
            int rowCnt = 0, colCnt = 0;
            for(j = 0; j < GRID_SIZE; j++) {
                rowCnt += grid[i][j] == cellType ? 1 : 0;
                colCnt += grid[j][i] == cellType ? 1 : 0;
            }
            if(rowCnt == GRID_SIZE || colCnt == GRID_SIZE)
                return true;
        }

        // main-diagonal
        int k = 0;
        while(k < GRID_SIZE && grid[k][k] == cellType) {
            k++;
        }

        // anti-diagonal
        i = 0; j = GRID_SIZE - 1;
        while(i < GRID_SIZE && j > -1 && grid[i][j] == cellType) {
            i++;
            j--;
        }

        return (k == GRID_SIZE) || (i == GRID_SIZE && j == -1);
    }

    void addCell(Cell cell, CellType cellType) {
        int row = cell.x(), col = cell.y();
        grid[row][col] = cellType;
        xCells += grid[row][col] == CellType.X ? 1 : 0;
        oCells += grid[row][col] == CellType.O ? 1 : 0;
        updateGridState();
    }

    void printGrid() {
        final int ind = 2 + GRID_SIZE + (GRID_SIZE - 1) + 2;
        for(int i = 0; i < ind; i++) {
            System.out.print('-');
        }
        System.out.println();
        for(int i = 0; i < GRID_SIZE; i++) {
            System.out.print("| ");
            for(int j = 0; j < GRID_SIZE; j++)
                System.out.print(grid[i][j].symbol() + " ");
            System.out.println('|');
        }
        for(int i = 0; i < ind; i++) {
            System.out.print('-');
        }
        System.out.println();
    }
}

public class Main {
    public static void main(String[] args) {
        new TicTacToe();
    }
}
