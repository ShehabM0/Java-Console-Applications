package battleship;

enum Ship {
    AIRCRAFT(5),
    BATTLESHIP(4),
    SUBMARINE(3),
    CRUISER(3),
    DESTROYER(2);

    private final int size;

    Ship(int cells) {
        this.size = cells;
    }

    @Override
    public String toString() {
        String name = name().toLowerCase();
        char firstChar = Character.toUpperCase(name.charAt(0));
        if(this == AIRCRAFT)
            return firstChar + name.substring(1) + " Carrier";
        return firstChar + name.substring(1);
    }

    int size() {
        return size;
    }
}
