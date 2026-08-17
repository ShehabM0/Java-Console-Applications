package tictactoe;

import java.util.*;

class BotPlayer implements Player {
    private final Grid grid;
    private final int gridSize;

    private final CellType cellType, userCellType;
    private final Difficulty difficulty;

    BotPlayer(Grid grid, CellType cellType, Difficulty difficulty) {
        this.grid = grid;
        this.gridSize = grid.getSize();

        this.cellType = cellType;
        this.userCellType = cellType == CellType.X ? CellType.O : CellType.X;

        this.difficulty = difficulty;
    }

    @Override
    public Cell makeMove() {
        System.out.printf("Making move level \"%s\"%n", difficulty.name().toLowerCase());
        if(difficulty == Difficulty.HARD) {
            return makeOptimalMove();
        }

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
        for (int i = 0; i < gridSize; i++)
            for (int j = 0; j < gridSize; j++)
                if (grid.isEmptyCell(i, j))
                    availableCells.add(new Cell(i, j));

        int availableCellsLen = availableCells.size();
        int idx = new Random().nextInt(availableCellsLen);

        return availableCells.get(idx);
    }

    public Cell makeWinningMove() {
        // row, col
        for (int i = 0; i < gridSize; i++) {
            int rowCnt = 0, colCnt = 0;
            int emptyRowCnt = 0, emptyColCnt = 0;
            Cell targetRowCell = null, targetColCell = null;
            for (int j = 0; j < gridSize; j++) {
                rowCnt += grid.getCellType(i, j) == cellType ? 1 : 0;
                colCnt += grid.getCellType(j, i) == cellType ? 1 : 0;

                if (grid.isEmptyCell(i, j)) {
                    emptyRowCnt++;
                    targetRowCell = new Cell(i, j);
                }
                if (grid.isEmptyCell(j, i)) {
                    emptyColCnt++;
                    targetColCell = new Cell(j, i);
                }
            }
            if (rowCnt == gridSize - 1 && emptyRowCnt == 1) {
                return targetRowCell;
            }
            if (colCnt == gridSize - 1 && emptyColCnt == 1) {
                return targetColCell;
            }
        }

        Cell targetDiagCell = null;
        // main-diagonal
        int diagCnt = 0, emptyDiagCnt = 0;
        for (int k = 0; k < gridSize; k++) {
            diagCnt += grid.getCellType(k, k) == cellType ? 1 : 0;

            if (grid.isEmptyCell(k, k)) {
                emptyDiagCnt++;
                targetDiagCell = new Cell(k, k);
            }
        }
        if (diagCnt == gridSize - 1 && emptyDiagCnt == 1)
            return targetDiagCell;

        diagCnt = 0; emptyDiagCnt = 0;
        // anti-diagonal
        for (int i = 0, j = gridSize - 1; i < gridSize && j > -1; i++, j--) {
            diagCnt += grid.getCellType(i, j) == cellType ? 1 : 0;

            if (grid.isEmptyCell(i, j)) {
                emptyDiagCnt++;
                targetDiagCell = new Cell(i, j);
            }
        }
        if (diagCnt == gridSize - 1 && emptyDiagCnt == 1)
            return targetDiagCell;

        return null;
    }

    public Cell makeBlockingMove() {
        // row, col
        for (int i = 0; i < gridSize; i++) {
            int rowCnt = 0, colCnt = 0;
            int emptyRowCnt = 0, emptyColCnt = 0;
            Cell targetRowCell = null, targetColCell = null;
            for (int j = 0; j < gridSize; j++) {
                rowCnt += grid.getCellType(i, j) == userCellType ? 1 : 0;
                colCnt += grid.getCellType(j, i) == userCellType ? 1 : 0;

                if (grid.isEmptyCell(i, j)) {
                    emptyRowCnt++;
                    targetRowCell = new Cell(i, j);
                }
                if (grid.isEmptyCell(j, i)) {
                    emptyColCnt++;
                    targetColCell = new Cell(j, i);
                }
            }
            if (rowCnt == gridSize - 1 && emptyRowCnt == 1) {
                return targetRowCell;
            }
            if (colCnt == gridSize - 1 && emptyColCnt == 1) {
                return targetColCell;
            }
        }

        Cell targetDiagCell = null;
        // main-diagonal
        int diagCnt = 0, emptyDiagCnt = 0;
        for (int k = 0; k < gridSize; k++) {
            diagCnt += grid.getCellType(k, k) == userCellType ? 1 : 0;

            if (grid.isEmptyCell(k, k)) {
                emptyDiagCnt++;
                targetDiagCell = new Cell(k, k);
            }
        }
        if (diagCnt == gridSize - 1 && emptyDiagCnt == 1)
            return targetDiagCell;

        diagCnt = 0; emptyDiagCnt = 0;
        // anti-diagonal
        for (int i = 0, j = gridSize - 1; i < gridSize && j > -1; i++, j--) {
            diagCnt += grid.getCellType(i, j) == userCellType ? 1 : 0;

            if (grid.isEmptyCell(i, j)) {
                emptyDiagCnt++;
                targetDiagCell = new Cell(i, j);
            }
        }
        if (diagCnt == gridSize - 1 && emptyDiagCnt == 1)
            return targetDiagCell;

        return null;
    }

    public Cell makeOptimalMove() {
        int bestScore = Integer.MIN_VALUE;
        Cell optimalCell = null;

        List<Cell> availableCells = grid.getEmptyCells();
        Collections.shuffle(availableCells);
        for(Cell cell : availableCells) {
            grid.addCell(cell, cellType);
            int score = minimax(true);
            if(score > bestScore) {
                bestScore = score;
                optimalCell = cell;
            }
            grid.emptyCell(cell);
        }

        return optimalCell;
    }

    public int minimax(boolean userTurn) {
        if(grid.getState() == GridState.X)
            return cellType == CellType.X ? 1 : -1;
        if(grid.getState() == GridState.O)
            return cellType == CellType.O ? 1 : -1;
        if(grid.getState() == GridState.DRAW)
            return 0;

        List<Cell> availableCells = grid.getEmptyCells();
        if(!userTurn) {
            int mx = Integer.MIN_VALUE;
            for(Cell cell : availableCells) {
                grid.addCell(cell, cellType);
                mx = Math.max(mx, minimax(true));
                grid.emptyCell(cell);
            }
            return mx;
        } else {
            int mn = Integer.MAX_VALUE;
            for(Cell cell : availableCells) {
                grid.addCell(cell, userCellType);
                mn = Math.min(mn, minimax(false));
                grid.emptyCell(cell);
            }
            return mn;
        }
    }
}
