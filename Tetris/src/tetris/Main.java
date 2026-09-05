package tetris;

class Main {
    public static void main(String[] args) {
        Grid grid;
        for(Tetromino tetromino : Tetromino.values()) {
            System.out.printf("##### %s #####%n", tetromino);
            grid = new Grid(tetromino);
            grid.display();
            int rotationsSize = tetromino.getStatesSize() - 1;
            while (rotationsSize-- > 0) {
                grid.rotate();
                System.out.println();
                grid.display();
            }
        }
    }
}
