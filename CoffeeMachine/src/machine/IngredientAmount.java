package machine;

class IngredientAmount {
    private final Ingredient ingredient;
    private int amount;

    IngredientAmount(Ingredient ingredient, int amount) {
        this.ingredient = ingredient;
        this.amount = amount;
    }

    Ingredient getIngredient() {
        return ingredient;
    }

    int getAmount() {
        return amount;
    }

    void consume(int amount) {
        this.amount -= amount;
    }
    void fill(int amount) {
        this.amount += amount;
    }
}
