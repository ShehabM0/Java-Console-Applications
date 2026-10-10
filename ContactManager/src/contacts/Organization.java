package contacts;

class Organization extends Contact {
    private String address;

    Organization(String name, String number, String address) {
        super(name, number);
        this.address = address;
    }

    public String getAddress() {
        return address.isEmpty() ? "[no data]" : address;
    }

    @Override
    public String toString() {
        return String.format(
                "Organization name: %s%n" +
                        "Address: %s%n" +
                        "Number: %s%n" +
                        "Time created: %s%n" +
                        "Time last edit: %s%n",
                this.getName(),
                this.getAddress(),
                this.getNumber(),
                this.getCreatedAt(),
                this.getUpdatedAt()
        );
    }
}
