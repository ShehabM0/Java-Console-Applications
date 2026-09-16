package budget;

enum PurchaseType {
    FOOD,
    CLOTHES,
    ENTERTAINMENT,
    OTHER;

    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
