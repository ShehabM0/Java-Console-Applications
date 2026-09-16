package budget;

enum Action {
    ADD_INCOME("Add income"),
    Add_PURCHASE("Add purchase"),
    SHOW("Show list of purchases"),
    BALANCE("Balance"),
    SAVE("Save"),
    LOAD("Load"),
    EXIT("Exit");

    private final String str;

    Action(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
