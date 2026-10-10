package contacts;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static ContactManager contactManager;

    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : null;
        contactManager = new ContactManager(fileName);

        Action action = getActionInput();
        while (action != Action.EXIT) {
            switch(action) {
                case ADD -> handleContactAddition();
                case LIST -> handleContactListing();
                case SEARCH -> handleContactSearch();
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

    private static void handleContactUpdate(Contact contact, int idx) {
        Object[] pair = getFieldInput(contact.getClass());
        Field field = (Field) pair[0];
        String fieldValue = pair[1].toString();
        contactManager.edit(idx, field, fieldValue);
    }

    private static void handleContactListing() {
        if(contactManager.getCount() == 0) {
            System.out.println("Contact list is empty!");
            return;
        }

        int idx = getContactListIndex(true) - 1;
        if(idx < 0)
            return;
        Contact contact = contactManager.getContact(idx);
        handleRecordMenu(contact, idx);
    }

    private static void handleContactSearch() {
        System.out.print("Enter search query: ");
        String query = getStrInput();
        List<Object[]> res = contactManager.search(query);
        List<Contact> contacts = res.stream()
                .map(pair -> (Contact) pair[0])
                .toList();

        System.out.printf("Found %d results:%n", res.size());
        contactManager.displayContactsList(contacts);

        System.out.println("\n[search] ");
        System.out.print("Enter action ([number], back, again): ");
        String searchAction = getSearchActionInput();

        if(searchAction.equalsIgnoreCase("again"))
            handleContactSearch();
        if(searchAction.equalsIgnoreCase("back"))
            return;
        int contactListIdx = Integer.parseInt(searchAction);
        while (contactListIdx < 1 || contactListIdx > res.size()) {
            System.out.println("Enter valid list number!");
            contactListIdx = getNumberInput();
        }

        Contact contact = (Contact) res.get(contactListIdx - 1)[0];
        int contactIdx = (int) res.get(contactListIdx - 1)[1];
        handleRecordMenu(contact, contactIdx);
    }

    private static void handleRecordMenu(Contact contact, int idx) {
        System.out.println(contact);
        System.out.print("[record] ");
        System.out.print("Enter action (edit, delete, menu): ");
        String recordMenuInput = getRecordMenuInput();
        while(!recordMenuInput.equalsIgnoreCase("menu")) {
            if(recordMenuInput.equalsIgnoreCase("delete")) {
                contactManager.remove(idx);
                return;
            }
            else {
                handleContactUpdate(contact, idx);
                System.out.println(contact);
            }
            System.out.print("[record] ");
            System.out.print("Enter action (edit, delete, menu): ");
            recordMenuInput = getRecordMenuInput();
        }
    }

    private static String getRecordMenuInput() {
        String in = getStrInput();
        while (!in.equalsIgnoreCase("edit") &&
                !in.equalsIgnoreCase("delete") &&
                !in.equalsIgnoreCase("menu")) {
            System.out.println("Enter valid record action!");
            in = getStrInput();
        }
        return in;
    }

    private static int getContactListIndex(boolean isInfo) {
        contactManager.list();
        System.out.println();
        System.out.print("[list] ");
        System.out.print(
                isInfo ? "Enter action ([number], back): " : "Select a record: "
        );
        String in = getStrInput();
        if(in.equalsIgnoreCase("back"))
            return -1;
        while (true) {
            try {
                int idx = Integer.parseInt(in);
                if(idx > 0 && idx <= contactManager.getCount())
                    return idx;
                else {
                    System.out.println("Enter valid number!");
                    in = getStrInput();
                }
            } catch (NumberFormatException _) {
                System.out.println("Enter valid number!");
                in = getStrInput();
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

    private static int getNumberInput() {
        String in = sc.nextLine();
        while (true) {
            try {
                return Integer.parseInt(in);
            } catch (NumberFormatException _) {
                System.out.println("Enter valid number!");
                in = sc.nextLine();
            }
        }
    }

    private static Action getActionInput() {
        System.out.print("[menu] ");
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

        System.out.printf("Select a field (%s",superFields[1].getName() + ", ");
        for(int i = 0; i < fields.length; i++)
            System.out.printf(
                    "%s%s",
                    fields[i].getName(),
                    (i == fields.length - 1) ? "" : ", "
            );
        System.out.printf(", %s): ", superFields[2].getName());

        String in = sc.nextLine();
        Field fieldInput = null;
        Field[] allFields = Stream.concat(
                Arrays.stream(superFields),
                Arrays.stream(fields)
        ).toArray(Field[]::new);
        while (fieldInput == null) {
            for (Field field : allFields) {
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

    private static String getSearchActionInput() {
        String searchAction = getStrInput().trim();
        while (true) {
            if(searchAction.equalsIgnoreCase("back") ||
                    searchAction.equalsIgnoreCase("again"))
                return searchAction;
            try {
                Integer.parseInt(searchAction);
                return searchAction;
            } catch (NumberFormatException _) {
                System.out.println("Enter valid search action!");
                searchAction = getStrInput().trim();
            }
        }
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
