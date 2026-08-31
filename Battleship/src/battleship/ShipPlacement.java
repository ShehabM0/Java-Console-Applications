package battleship;

record ShipPlacement(Ship ship, ShipCells shipCells) {
    ShipPlacement {
        if(!isValidCordsLength(ship, shipCells))
            throw new IllegalArgumentException("Error! Wrong length of the " + ship + "! Try again:");
    }

    private boolean isValidCordsLength(Ship ship, ShipCells shipCells) {
        return ship.size() == shipCells.getCordsLen();
    }
}
