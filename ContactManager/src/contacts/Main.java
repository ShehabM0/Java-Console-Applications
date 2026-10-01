package contacts;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final ContactManager contactManager = new ContactManager();

    public static void main(String[] args) {
        Action action = getActionInput();
        while (action != Action.EXIT) {
            switch(action) {
                case ADD -> handleContactAddition();
                case REMOVE -> handleContactRemoval();
                case EDIT -> handleContactUpdate();
                case INFO -> handleContactListing();
                case COUNT -> contactManager.count();
            }
            System.out.println();
            action = getActionInput();
        }
        sc.close();
    }

    private static void handleContactAddition() {
        Class<?> contactType = getContactTypeInput();

        String name, number;
        String surname = "", birth = "", gender = "";
        String address = "";
        System.out.printf(
                "Enter the%sname: ",
                contactType == Person.class ? " " : " organization "
        );
        name = sc.nextLine();
        if(name.isBlank()) {
            System.out.println("Bad name!");
            name = "";
        }
        if(contactType == Person.class) {
            System.out.print("Enter the surname: "); surname = sc.nextLine();
            if(surname.isBlank()) {
                System.out.println("Bad surname!");
                surname = "";
            }
            System.out.print("Enter the birth date: "); birth = sc.nextLine();
            if(birth.isBlank()) {
                System.out.println("Bad birth date!");
                birth = "";
            }
            System.out.print("Enter the gender(M, F): "); gender = sc.nextLine();
            if(gender.isBlank() || !Validator.validateGender(gender)) {
                System.out.println("Bad gender!");
                gender = "";
            }
        } else {
            System.out.print("Enter the address: "); address = sc.nextLine();
            if(address.isBlank()) {
                System.out.println("Bad address!");
                address = "";
            }
        }
        System.out.print("Enter the number: "); number = getStrInput();
        String[] contactNumber = number.split("\\s+|-");
        boolean isValidNumber = Validator.validateNumber(contactNumber);
        if(!isValidNumber) {
            System.out.println("Wrong number format!");
            number = "";
        }

        if(contactType == Person.class)
            contactManager.addPerson(name, number, surname, birth, gender);
        else
            contactManager.addOrg(name, number, address);
    }

    private static void handleContactUpdate() {
        if(contactManager.getCount() == 0) {
            System.out.println("No records to edit!");
            return;
        }

        int idx = getContactListIndex(false) - 1;
        Class<?> contactType = contactManager.getContactType(idx);

        Object[] pair = getFieldInput(contactType);
        Field field = (Field) pair[0];
        String fieldValue = pair[1].toString();
        contactManager.edit(idx, field, fieldValue);
    }

    private static void handleContactRemoval() {
        if(contactManager.getCount() == 0) {
            System.out.println("No records to remove!");
            return;
        }

        int idx = getContactListIndex(false) - 1;
        contactManager.remove(idx);
    }

    private static void handleContactListing() {
        if(contactManager.getCount() == 0) {
            System.out.println("Contact list is empty!");
            return;
        }

        int idx = getContactListIndex(true) - 1;
        contactManager.list(idx);
    }

    private static int getContactListIndex(boolean isInfo) {
        contactManager.list();
        System.out.print(
                isInfo ? "Enter index to show info: " : "Select a record: "
        );
        String in = sc.nextLine();
        while (true) {
            try {
                int idx = Integer.parseInt(in);
                if(idx > 0 && idx <= contactManager.getCount())
                    return idx;
                else {
                    System.out.println("Enter valid number!");
                    in = sc.nextLine();
                }
            } catch (NumberFormatException _) {
                System.out.println("Enter valid number!");
                in = sc.nextLine();
            }
        }
    }

    private static String getStrInput() {
        String in = sc.nextLine();
        while(in.isBlank()) {
            System.out.println("Enter a non-empty string!");
            in = sc.nextLine();
        }
        return in;
    }

    private static Action getActionInput() {
        System.out.print("Enter action (");
        for(Action action : Action.values()) {
            System.out.printf(
                    "%s%s",
                    action,
                    (action.ordinal() == Action.values().length - 1) ? "): " : ", "
            );
        }

        String in = sc.nextLine();
        while(true) {
            try {
                return Action.valueOf(in.trim().toUpperCase());
            } catch (IllegalArgumentException _) {
                System.out.println("Invalid action!");
                in = sc.nextLine();
            }
        }
    }

    private static Object[] getFieldInput(Class<?> contactType) {
        Field[] superFields = contactType.getSuperclass().getDeclaredFields();
        Field[] fields = contactType.getDeclaredFields();

        System.out.printf(
            "Select a field (%s",
            contactType == Person.class ?
                    superFields[0].getName() + ", " :
                    ""
        );
        for(int i = 0; i < fields.length; i++)
            System.out.printf(
                    "%s%s",
                    fields[i].getName(),
                    (i == fields.length - 1) ? "" : ", "
            );
        System.out.printf(", %s): ", superFields[1].getName());

        String in = sc.nextLine();
        Field fieldInput = null;
        Field[] allFields = Stream.concat(
                Arrays.stream(superFields),
                Arrays.stream(fields)
        ).toArray(Field[]::new);
        while (fieldInput == null) {
            for (Field field : allFields) {
                if(contactType == Organization.class && field.getName().equals("name"))
                    continue;
                if(field.getName().equals(in.trim().toLowerCase()))
                    fieldInput = field;
            }
            if(fieldInput == null) {
                System.out.println("Enter valid field!");
                in = sc.nextLine();
            }
        }

        System.out.printf("Enter %s: ", fieldInput.getName());
        String fieldValue = getStrInput();

        return new Object[]{fieldInput, fieldValue};
    }

    private static Class<?> getContactTypeInput() {
        String personClassName = Person.class.getSimpleName().toLowerCase(),
                orgClassName = Organization.class.getSimpleName().toLowerCase();
        System.out.printf("Enter the type (%s, %s): ", personClassName, orgClassName);

        String in = sc.nextLine();
        while(true) {
            in = in.trim().toLowerCase();
            if(in.equals(personClassName))
                return Person.class;
            else if(in.equals(orgClassName))
                return Organization.class;
            else {
                System.out.println("Enter valid contact type!");
                in = sc.nextLine();
            }
        }
    }
}
