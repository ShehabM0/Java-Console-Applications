package contacts;

class Person extends Contact {
    private String surname, birth, gender;

    Person(String name, String number, String surname, String birth, String gender) {
        super(name, number);
        this.surname = surname;
        this.birth = birth;
        this.gender = gender.toUpperCase();
    }

    public String getSurname() {
        return surname.isEmpty() ? "[no data]" : surname;
    }

    public String getBirth() {
        return birth.isEmpty() ? "[no data]" : birth;
    }

    public String getGender() {
        return gender.isEmpty() ? "[no data]" : gender;
    }

    @Override
    public String toString() {
        return String.format(
                "Name: %s%n" +
                        "Surname: %s%n" +
                        "Birth date: %s%n" +
                        "Gender: %s%n" +
                        "Number: %s%n" +
                        "Time created: %s%n" +
                        "Time last edit: %s%n",
                this.getName(),
                this.getSurname(),
                this.getBirth(),
                this.getGender(),
                this.getNumber(),
                this.getCreatedAt(),
                this.getUpdatedAt()
        );
    }
}
