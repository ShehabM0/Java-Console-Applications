package battleship;

class Player {
    private final String name;
    private final Grid grid;
    private final Grid opponentViewGrid;

    Player(String name) {
        this.name = name;
        grid = new Grid();
        opponentViewGrid = new Grid();
    }

    String getName() {
        return name;
    }

    Grid getGrid() {
        return grid;
    }

    Grid getOpponentViewGrid() {
        return opponentViewGrid;
    }

    void displayGrids() {
        opponentViewGrid.display();
        System.out.println("-".repeat(2 * Grid.SIZE + 1));
        grid.display();
        System.out.println();
    }

    ShootResult shoot(Player player, Cell cell) {
        ShootResult shootResult = player.getGrid().shoot(cell);
        this.getOpponentViewGrid().mirror(cell, shootResult);
        return shootResult;
    }
}
