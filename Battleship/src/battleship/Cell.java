package battleship;

record Cell(int x, int y) {
    static Cell parseCell(String cell) {
        if(!isValidString(cell))
            throw new IllegalArgumentException("Error! You entered the wrong coordinates! Try again:");

        cell = cell.toUpperCase();
        int x = cell.charAt(0) - 'A';
        int y = Integer.parseInt(cell.substring(1)) - 1;
        return new Cell(x, y);
    }

    static boolean isValidString(String cell) {
        return cell.matches("(?i)[A-J]([1-9]|10)");
    }
}
