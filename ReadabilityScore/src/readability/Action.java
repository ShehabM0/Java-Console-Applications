package readability;

enum Action {
    ARI,
    FK,
    SMOG,
    CL,
    ALL;

    @Override
    public String toString() {
        return this == ALL ? this.name().toLowerCase() : this.name();
    }
}
