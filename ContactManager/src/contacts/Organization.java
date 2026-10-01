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
}
