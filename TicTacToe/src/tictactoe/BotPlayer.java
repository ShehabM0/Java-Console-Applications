package tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BotPlayer implements Player {

    private final int GRID_SIZE;
    private final CellType[][] grid;
    private final CellType cellType, userCellType;
    private final Difficulty difficulty;

    BotPlayer(CellType[][] grid, CellType cellType, Difficulty difficulty) {
        this.grid = grid;
        this.cellType = cellType; // X, O
        this.userCellType = cellType == CellType.X ? CellType.O : CellType.X;
        this.difficulty = difficulty;

        GRID_SIZE = grid.length;
    }

    @Override
    public Cell makeMove() {
        System.out.printf("Making move level \"%s\"", difficulty.name().toLowerCase());
        if(difficulty == Difficulty.MEDIUM) {
            Cell pickedCell = makeWinningMove();
            if(pickedCell != null)
                return pickedCell;

            pickedCell = makeBlockingMove();
            if(pickedCell != null)
                return pickedCell;

        }

        return makeRandomMove();
    }

    public Cell makeRandomMove() {
        List<Cell> availableCells = new ArrayList<>();
        for (int row = 0; row < GRID_SIZE; row++)
            for (int col = 0; col < GRID_SIZE; col++)
                if (grid[row][col] == CellType.EMPTY)
                    availableCells.add(new Cell(row, col));

        int availableCellsLen = availableCells.size();
        int idx = new Random().nextInt(availableCellsLen);

        return availableCells.get(idx);
    }

    public Cell makeWinningMove() {
        // row, col
        for (int i = 0; i < GRID_SIZE; i++) {
            int rowCnt = 0, colCnt = 0;
            int emptyRowCnt = 0, emptyColCnt = 0;
            Cell targetRowCell = null, targetColCell = null;
            for (int j = 0; j < GRID_SIZE; j++) {
                rowCnt += grid[i][j] == cellType ? 1 : 0;
                colCnt += grid[j][i] == cellType ? 1 : 0;

                if (grid[i][j] == CellType.EMPTY) {
                    emptyRowCnt++;
                    targetRowCell = new Cell(i, j);
                }
                if (grid[j][i] == CellType.EMPTY) {
                    emptyColCnt++;
                    targetColCell = new Cell(j, i);
                }
            }
            if (rowCnt == GRID_SIZE - 1 && emptyRowCnt == 1) {
                return targetRowCell;
            }
            if (colCnt == GRID_SIZE - 1 && emptyColCnt == 1) {
                return targetColCell;
            }
        }

        Cell targetDiagCell = null;
        // main-diagonal
        int diagCnt = 0, emptyDiagCnt = 0;
        for (int k = 0; k < GRID_SIZE; k++) {
            diagCnt += grid[k][k] == cellType ? 1 : 0;

            if (grid[k][k] == CellType.EMPTY) {
                emptyDiagCnt++;
                targetDiagCell = new Cell(k, k);
            }
        }
        if (diagCnt == GRID_SIZE - 1 && emptyDiagCnt == 1)
            return targetDiagCell;

        diagCnt = 0; emptyDiagCnt = 0;
        // anti-diagonal
        for (int i = 0, j = GRID_SIZE - 1; i < GRID_SIZE && j > -1; i++, j--) {
            diagCnt += grid[i][j] == cellType ? 1 : 0;

            if (grid[i][j] == CellType.EMPTY) {
                emptyDiagCnt++;
                targetDiagCell = new Cell(i, j);
            }
        }
        if (diagCnt == GRID_SIZE - 1 && emptyDiagCnt == 1)
            return targetDiagCell;

        return null;
    }

    public Cell makeBlockingMove() {
        // row, col
        for (int i = 0; i < GRID_SIZE; i++) {
            int rowCnt = 0, colCnt = 0;
            int emptyRowCnt = 0, emptyColCnt = 0;
            Cell targetRowCell = null, targetColCell = null;
            for (int j = 0; j < GRID_SIZE; j++) {
                rowCnt += grid[i][j] == userCellType ? 1 : 0;
                colCnt += grid[j][i] == userCellType ? 1 : 0;

                if (grid[i][j] == CellType.EMPTY) {
                    emptyRowCnt++;
                    targetRowCell = new Cell(i, j);
                }
                if (grid[j][i] == CellType.EMPTY) {
                    emptyColCnt++;
                    targetColCell = new Cell(j, i);
                }
            }
            if (rowCnt == GRID_SIZE - 1 && emptyRowCnt == 1) {
                return targetRowCell;
            }
            if (colCnt == GRID_SIZE - 1 && emptyColCnt == 1) {
                return targetColCell;
            }
        }

        Cell targetDiagCell = null;
        // main-diagonal
        int diagCnt = 0, emptyDiagCnt = 0;
        for (int k = 0; k < GRID_SIZE; k++) {
            diagCnt += grid[k][k] == userCellType ? 1 : 0;

            if (grid[k][k] == CellType.EMPTY) {
                emptyDiagCnt++;
                targetDiagCell = new Cell(k, k);
            }
        }
        if (diagCnt == GRID_SIZE - 1 && emptyDiagCnt == 1)
            return targetDiagCell;

        diagCnt = 0; emptyDiagCnt = 0;
        // anti-diagonal
        for (int i = 0, j = GRID_SIZE - 1; i < GRID_SIZE && j > -1; i++, j--) {
            diagCnt += grid[i][j] == userCellType ? 1 : 0;

            if (grid[i][j] == CellType.EMPTY) {
                emptyDiagCnt++;
                targetDiagCell = new Cell(i, j);
            }
        }
        if (diagCnt == GRID_SIZE - 1 && emptyDiagCnt == 1)
            return targetDiagCell;

        return null;
    }
}
