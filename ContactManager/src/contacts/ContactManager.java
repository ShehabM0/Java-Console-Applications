package contacts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class ContactManager {
    private final String fileName;

    private final List<Contact> contacts;

    ContactManager(String fileName) {
        contacts = new ArrayList<>();
        this.fileName = fileName;
        if (fileName != null)
            load();
    }

    void addPerson(String... contactInfo) {
        String name = contactInfo[0],
                number = contactInfo[1],
                surname = contactInfo[2],
                birth = contactInfo[3],
                gender = contactInfo[4];

        contacts.add(new Person(name, number, surname, birth, gender));
        System.out.println("The record added.");

        save();
    }

    void addOrg(String... contactInfo) {
        String name = contactInfo[0],
                number = contactInfo[1],
                address = contactInfo[2];

        contacts.add(new Organization(name, number, address));
        System.out.println("The record added.");

        save();
    }

    void count() {
        System.out.printf("The Phone Book has %d records.%n", contacts.size());
    }

    void list() {
        for(int i = 0; i < contacts.size(); i++) {
            String name = contacts.get(i).getName();
            if(contacts.get(i) instanceof Person person)
                name += " " + person.getSurname();
            System.out.printf("%d. %s%n", i + 1, name);
        }
    }

    void list(int idx) {
        Contact contact = contacts.get(idx);
        if(contact instanceof Person person) {
            System.out.printf("Name: %s%n", contact.getName());
            System.out.printf("Surname: %s%n", person.getSurname());
            System.out.printf("Birth date: %s%n", person.getBirth());
            System.out.printf("Gender: %s%n", person.getGender());
        } else if(contact instanceof Organization organization) {
            System.out.printf("Organization name: %s%n", contact.getName());
            System.out.printf("Address: %s%n", organization.getAddress());
        }
        System.out.printf("Number: %s%n", contact.getNumber());
        System.out.printf("Time created: %s%n", contact.getCreatedAt());
        System.out.printf("Time last edit: %s%n", contact.getUpdatedAt());
    }

    void edit(int idx, Field field, String value) {
        Contact contact = contacts.get(idx);

        if(field.getName().equalsIgnoreCase("number")) {
            String[] contactNumber = value.split("\\s+|-");
            boolean isValidNumber = Validator.validateNumber(contactNumber);
            if(!isValidNumber) {
                System.out.println("Wrong number format!");
                value = "";
            }
            contact.setNumber(value);
        } else {
            Field[] superFields = contact.getClass().getSuperclass().getDeclaredFields();
            Field[] fields = contact.getClass().getDeclaredFields();
            Field[] allFields = Stream.concat(
                    Arrays.stream(superFields),
                    Arrays.stream(fields)
            ).toArray(Field[]::new);

            for(Field fieldi : allFields) {
                if(field.getName().equalsIgnoreCase(fieldi.getName())) {
                    try {
                        fieldi.setAccessible(true);
                        fieldi.set(contact, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        contact.updateUpdateAt();
        System.out.println("Saved");

        save();
    }

    void remove(int idx) {
        contacts.remove(idx);
        System.out.println("The record removed!");

        save();
    }

    List<Object[]> search(String query) {
        List<Object[]> res = new ArrayList<>();

        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);

            Set<String> excludedFields = Set.of("serialVersionUID", "createdAt", "updatedAt");
            Field[] superFields = contact.getClass().getSuperclass().getDeclaredFields();
            Field[] fields = contact.getClass().getDeclaredFields();
            Field[] allFields = Stream.concat(
                    Arrays.stream(superFields),
                    Arrays.stream(fields)
            ).toArray(Field[]::new);

            String fieldsValue = "";
            for(Field field : allFields) {
                if(excludedFields.contains(field.getName()))
                    continue;
                try {
                    field.setAccessible(true);
                    Object value = field.get(contact);
                    fieldsValue += value + " ";
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            if (pattern.matcher(fieldsValue).find())
                res.add(new Object[]{contact, i});
        }

        return res;
    }

    private void load() {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            List<Contact> loaded = (List<Contact>) in.readObject();
            contacts.addAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() {
        if (fileName == null) {
            return;
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(contacts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    int getCount() {
        return contacts.size();
    }

    Class<?> getContactType(int idx) {
        return contacts.get(idx).getClass();
    }

    Contact getContact(int idx) {
        return contacts.get(idx);
    }

    void displayContactsList(List<Contact> contacts) {
        for(int i = 0; i < contacts.size(); i++) {
            String name = contacts.get(i).getName();
            if(contacts.get(i) instanceof Person person)
                name += " " + person.getSurname();
            System.out.printf("%d. %s%n", i + 1, name);
        }
    }
}
