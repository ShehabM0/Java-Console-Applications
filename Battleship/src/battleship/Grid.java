package battleship;

class Grid {
    private final int SHIP_COUNT = Ship.values().length;
    private final int SIZE = 10;
    private final ShipCell[][] grid = new ShipCell[SIZE][SIZE];
    private int sankShips;

    Grid() {
        sankShips = 0;
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                grid[i][j] = new ShipCell(null, CellType.EMPTY);
    }

    void placeShip(ShipPlacement shipPlacement, ShipCellPair shipCellPair) {
        Cell from = shipCellPair.from(), to = shipCellPair.to();
        if(isAdjacentShip(shipCellPair))
            throw new IllegalArgumentException("Error! You placed it too close to another one. Try again:");

        for(int i = from.x(); i <= to.x(); i++)
            for(int j = from.y(); j <= to.y(); j++)
                grid[i][j] = new ShipCell(shipPlacement, CellType.SHIP);
    }

    ShootResult shoot(Cell cell) {
        CellType cellType = getCellType(cell);
        if(cellType == CellType.EMPTY || cellType == CellType.MISS) {
            miss(cell);
            return ShootResult.MISS;
        }

        if(cellType == CellType.HIT)
            return ShootResult.HIT;

        // SHIP
        ShipPlacement shipPlacement = getShipPlacement(cell);
        hit(cell);
        if(isShipSunk(shipPlacement)) {
            sankShips++;
            return sankShips == SHIP_COUNT ? ShootResult.WON : ShootResult.SANK;
        }
        return ShootResult.HIT;
    }

    void mirror(Cell cell, ShootResult shootResult) {
        if(shootResult != ShootResult.MISS)
            hit(cell);
        else
            miss(cell);
    }

    CellType getCellType(Cell cell) {
        return grid[cell.x()][cell.y()].cellType();
    }

    ShipPlacement getShipPlacement(Cell cell) {
        return grid[cell.x()][cell.y()].shipPlacement();
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
                System.out.print(grid[i][j].cellType().symbol() + " ");
            System.out.println();
        }
    }

    private void hit(Cell cell) {
        ShipCell shipCell = grid[cell.x()][cell.y()];
        grid[cell.x()][cell.y()] = new ShipCell(
                shipCell.shipPlacement(),
                CellType.HIT
        );
    }

    private void miss(Cell cell) {
        ShipCell shipCell = grid[cell.x()][cell.y()];
        grid[cell.x()][cell.y()] = new ShipCell(
                shipCell.shipPlacement(),
                CellType.MISS
        );
    }

    private boolean isShipSunk(ShipPlacement shipPlacement) {
        Cell from = shipPlacement.shipCellPair().from(),
                to = shipPlacement.shipCellPair().to();

        int hitCells = 0;
        for(int i = from.x(); i <= to.x(); i++)
            for(int j = from.y(); j <= to.y(); j++)
                if(grid[i][j].cellType().isHit())
                    hitCells++;

        return shipPlacement.ship().size() == hitCells;
    }

    private boolean isAdjacentShip(ShipCellPair shipCellPair) {
        Cell from = shipCellPair.from(), to = shipCellPair.to();
        for(int i = from.x(); i <= to.x(); i++)
            for(int j = from.y(); j <= to.y(); j++)
                if(isAdjacentCell(new Cell(i, j)))
                    return true;
        return false;
    }

    private boolean isAdjacentCell(Cell cell) {
        final int[] dx = new int[]{ -1, -1, -1, 0, 0, 1, 1, 1 },
                    dy = new int[]{ -1,  0,  1, -1, 1, -1, 0, 1 };
        for(int d = 0; d < 8; d++) {
            int nr = cell.x() + dx[d];
            int nc = cell.y() + dy[d];
            if(isValidBoundary(new Cell(nr, nc)) && !grid[nr][nc].cellType().isEmpty())
                return true;
        }
        return false;
    }

    private boolean isValidBoundary(Cell cell) {
        return cell.x() > -1 && cell.x() < SIZE && cell.y() > -1 && cell.y() < SIZE;
    }
}
