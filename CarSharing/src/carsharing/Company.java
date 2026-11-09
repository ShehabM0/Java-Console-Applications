package carsharing;

record Company(int id, String name) {
    @Override
    public String toString() {
        return String.format("%d. %s", id, name);
    }
}

