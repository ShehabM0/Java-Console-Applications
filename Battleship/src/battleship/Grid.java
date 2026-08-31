package battleship;

class Grid {
    private final int SIZE = 10;
    private final CellType[][] grid = new CellType[SIZE][SIZE];

    Grid() {
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                grid[i][j] = CellType.EMPTY;
    }

    void placeShip(ShipCells shipCells) {
        Cell from = shipCells.from(), to = shipCells.to();
        if(isShipAdjacent(shipCells))
            throw new IllegalArgumentException("Error! You placed it too close to another one. Try again:");

        for(int i = from.x(); i <= to.x(); i++)
            for(int j = from.y(); j <= to.y(); j++)
                grid[i][j] = CellType.SHIP;
    }

    private boolean isShipAdjacent(ShipCells shipCells) {
        Cell from = shipCells.from(), to = shipCells.to();
        for(int i = from.x(); i <= to.x(); i++)
            for(int j = from.y(); j <= to.y(); j++)
                if(isCellAdjacent(new Cell(i, j)))
                    return true;
        return false;
    }

    private boolean isCellAdjacent(Cell cell) {
        final int[] dx = new int[]{ -1, -1, -1, 0, 0, 1, 1, 1 },
                    dy = new int[]{ -1,  0,  1, -1, 1, -1, 0, 1 };
        for(int d = 0; d < 8; d++) {
            int nr = cell.x() + dx[d];
            int nc = cell.y() + dy[d];
            if(isValidBoundary(new Cell(nr, nc)) && grid[nr][nc] != CellType.EMPTY)
                return true;
        }
        return false;
    }

    private boolean isValidBoundary(Cell cell) {
        return cell.x() > -1 && cell.x() < SIZE && cell.y() > -1 && cell.y() < SIZE;
    }

    void display() {
        for(int j = 0; j < SIZE; j++) {
            if(j == 0)
                System.out.print("  ");
            System.out.print(j + 1 + " ");
            if(j == SIZE - 1)
                System.out.println();
        }
        for(int i = 0, c = 'A'; i < SIZE; i++, c++) {
            System.out.print((char) c + " ");
            for(int j = 0; j < SIZE; j++)
                System.out.print(grid[i][j].symbol() + " ");
            System.out.println();
        }
    }
}
