package contacts;

enum Action {
    ADD,
    REMOVE,
    EDIT,
    COUNT,
    INFO,
    EXIT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
