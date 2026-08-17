package tictactoe;

import java.util.ArrayList;
import java.util.List;

class Grid {
    private final int size = 3;
    private final CellType[][] grid = new CellType[size][size];

    private GridState state ;
    private int xCells, oCells;

    Grid() {
        reset();
    }

    void reset() {
        state = GridState.NOT_FINISHED;
        xCells = 0; oCells = 0;
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                grid[row][col] = CellType.EMPTY;
    }

    void updateState() {
        boolean xWins = false, oWins = false;
        if(scan(CellType.X))
            xWins = true;
        if(scan(CellType.O))
            oWins = true;

        if(xWins) {
            state = GridState.X;
        } else if(oWins) {
            state = GridState.O;
        } else if(xCells + oCells == size * size) {
            state = GridState.DRAW;
        } else {
            state = GridState.NOT_FINISHED;
        }
    }

    boolean scan(CellType cellType) {
        int i, j;
        // row, col
        for(i = 0; i < size; i++) {
            int rowCnt = 0, colCnt = 0;
            for(j = 0; j < size; j++) {
                rowCnt += grid[i][j] == cellType ? 1 : 0;
                colCnt += grid[j][i] == cellType ? 1 : 0;
            }
            if(rowCnt == size || colCnt == size)
                return true;
        }

        // main-diagonal
        int k = 0;
        while(k < size && grid[k][k] == cellType) {
            k++;
        }

        // anti-diagonal
        i = 0; j = size - 1;
        while(i < size && j > -1 && grid[i][j] == cellType) {
            i++;
            j--;
        }

        return (k == size) || (i == size && j == -1);
    }

    void addCell(Cell cell, CellType cellType) {
        int row = cell.x(), col = cell.y();
        grid[row][col] = cellType;
        xCells += grid[row][col] == CellType.X ? 1 : 0;
        oCells += grid[row][col] == CellType.O ? 1 : 0;
        updateState();
    }

    void emptyCell(Cell cell) {
        int row = cell.x(), col = cell.y();
        oCells -= grid[row][col] == CellType.O ? 1 : 0;
        xCells -= grid[row][col] == CellType.X ? 1 : 0;
        grid[row][col] = CellType.EMPTY;
        updateState();
    }

    void print() {
        final int ind = 2 + size + (size - 1) + 2;
        for(int i = 0; i < ind; i++) {
            System.out.print('-');
        }
        System.out.println();
        for(int i = 0; i < size; i++) {
            System.out.print("| ");
            for(int j = 0; j < size; j++)
                System.out.print(grid[i][j].symbol() + " ");
            System.out.println('|');
        }
        for(int i = 0; i < ind; i++) {
            System.out.print('-');
        }
        System.out.println();
    }

    public List<Cell> getEmptyCells() {
        List<Cell> availableCells = new ArrayList<>();
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                if (grid[row][col] == CellType.EMPTY)
                    availableCells.add(new Cell(row, col));
        return availableCells;
    }

    int getSize() {
        return size;
    }

    GridState getState() {
        return state;
    }

    CellType getCellType(int row, int col) {
        return grid[row][col];
    }

    boolean isEmptyCell(int row, int col) {
        return grid[row][col] == CellType.EMPTY;
    }
}
