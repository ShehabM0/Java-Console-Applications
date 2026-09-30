package contacts;

enum Action {
    ADD,
    REMOVE,
    EDIT,
    COUNT,
    LIST,
    EXIT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
