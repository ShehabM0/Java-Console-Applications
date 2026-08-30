package battleship;

record Cell(int x, int y) {
    static Cell parseCell(String cell) {
        if(!isValidString(cell))
            throw new IllegalArgumentException("Invalid cell: " + cell + "!");

        cell = cell.toUpperCase();
        int x = cell.charAt(0) - 'A';
        int y = Integer.parseInt(cell.substring(1)) - 1;
        return new Cell(x, y);
    }

    private static boolean isValidString(String cell) {
        return cell.matches("(?i)[A-J]([1-9]|10)");
    }
}
