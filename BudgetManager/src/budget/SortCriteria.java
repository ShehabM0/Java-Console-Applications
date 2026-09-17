package budget;

enum SortCriteria {
    ALL("Sort all purchases"),
    GROUPED_BY_TYPE("Sort by type"),
    FILTERED_BY_TYPE("Sort certain type");

    private final String str;

    SortCriteria(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}