package contacts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ContactManager {
    private final Scanner sc;
    private final List<Contact> contacts;

    ContactManager(Scanner sc) {
        this.sc = sc;
        contacts = new ArrayList<>();
    }

    void add(String... contactInfo) {
        String name = contactInfo[0],
                surname = contactInfo[1],
                number = contactInfo[2];

        String[] contactNumber = number.split("\\s+|-");
        boolean isValidNumber = ContactNumber.validateNumber(contactNumber);
        if(!isValidNumber) {
            System.out.println("Wrong number format!");
            number = "";
        }

        contacts.add(new Contact(name, surname, number));
        System.out.println("The record added.");
    }

    void count() {
        System.out.printf("The Phone Book has %d records.%n", contacts.size());
    }

    void list() {
        for(int i = 0; i < contacts.size(); i++) {
            System.out.printf(
                    "%d. %s %s, %s%n",
                    i + 1,
                    contacts.get(i).getName(),
                    contacts.get(i).getSurname(),
                    contacts.get(i).getNumber().isEmpty() ? "[no number]" : contacts.get(i).getNumber()
            );
        }
    }

    void edit(int idx, Field field, String value) {
        Contact contact = contacts.get(idx);

        if(field.getName().equals("name"))
            contact.setName(value);
        else if(field.getName().equals("surname"))
            contact.setSurname(value);
        else {
            String[] contactNumber = value.split("\\s+|-");
            boolean isValidNumber = ContactNumber.validateNumber(contactNumber);
            if(!isValidNumber) {
                System.out.println("Wrong number format!");
                value = "";
            }
            contact.setNumber(value);
        }

        System.out.println("The record updated!");
    }

    void remove(int idx) {
        contacts.remove(idx);
        System.out.println("The record removed!");
    }

    int getCount() {
        return contacts.size();
    }
}
