package battleship;

record ShipPlacement(Ship ship, ShipCellPair shipCellPair) {
    ShipPlacement {
        if(!isValidCordsLength(ship, shipCellPair))
            throw new IllegalArgumentException("Error! Wrong length of the " + ship + "! Try again:");
    }

    private boolean isValidCordsLength(Ship ship, ShipCellPair shipCellPair) {
        return ship.size() == shipCellPair.getCordsLen();
    }
}
