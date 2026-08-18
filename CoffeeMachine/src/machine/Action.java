package machine;

enum Action {
    BUY,
    FILL,
    TAKE,
    CLEAN,
    REMAINING,
    EXIT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
