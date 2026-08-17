package tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class BotPlayer implements Player {

    private final int GRID_SIZE;
    private final CellType[][] grid;

    BotPlayer(CellType[][] grid) {
        this.grid = grid;
        GRID_SIZE = grid.length;
    }

    @Override
    public Cell makeMove() {
        System.out.println("Making move level \"easy\"");
        List<Cell> availableCells = new ArrayList<>();
        for (int row = 0; row < GRID_SIZE; row++)
            for (int col = 0; col < GRID_SIZE; col++)
                if (grid[row][col] == CellType.EMPTY)
                    availableCells.add(new Cell(row, col));

        int availableCellsLen = availableCells.size();
        int idx = new Random().nextInt(availableCellsLen);
        Cell pickedCell = availableCells.get(idx);

        return pickedCell;
    }
}
