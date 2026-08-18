package machine;

enum Ingredient {
    WATER("ml"),
    MILK("ml"),
    BEANS("g");

    private final String unit;

    Ingredient(String unit) {
        this.unit = unit;
    }

    String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return (this.name().equals("BEANS")) ? "coffee beans" : this.name().toLowerCase();
    }
}
