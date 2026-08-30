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
        shipCells.displayCordsInfo();
        for(int i = from.x(); i <= to.x(); i++)
            for(int j = from.y(); j <= to.y(); j++)
                grid[i][j] = CellType.SHIP;
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
