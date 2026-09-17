package budget;

record Purchase(String item, double price) implements Comparable<Purchase> {
    @Override
    public int compareTo(Purchase p) {
        return Double.compare(p.price, price);
    }
}
