package contacts;

enum Action {
    ADD,
    LIST,
    SEARCH,
    COUNT,
    EXIT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
