package contacts;

import java.lang.reflect.Field;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final ContactManager contactManager = new ContactManager(sc);

    public static void main(String[] args) {
        Action action = getActionInput();
        while (action != Action.EXIT) {
            switch(action) {
                case ADD -> handleContactAddition();
                case REMOVE -> handleContactRemoval();
                case EDIT -> handleContactUpdate();
                case COUNT -> contactManager.count();
                case LIST -> contactManager.list();
            }
            action = getActionInput();
        }
        sc.close();
    }

    private static void handleContactAddition() {
        String name, surname, number;
        System.out.print("Enter the name: "); name = getStrInput();
        System.out.print("Enter the surname: "); surname = getStrInput();
        System.out.print("Enter the number: "); number = getStrInput();
        contactManager.add(name, surname, number);
    }

    private static void handleContactUpdate() {
        if(contactManager.getCount() == 0) {
            System.out.println("No records to edit!");
            return;
        }

        int idx = getContactListIndex() - 1;
        Object[] pair = getFieldInput();
        Field field = (Field) pair[0];
        String fieldValue = pair[1].toString();
        contactManager.edit(idx, field, fieldValue);
    }

    private static void handleContactRemoval() {
        if(contactManager.getCount() == 0) {
            System.out.println("No records to remove!");
            return;
        }

        int idx = getContactListIndex() - 1;
        contactManager.remove(idx);
    }

    private static int getContactListIndex() {
        contactManager.list();
        System.out.print("Select a record: ");
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
        while(in.isEmpty()) {
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
                    (action.ordinal() == Action.values().length - 1) ? "):\n" : ", "
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

    private static Object[] getFieldInput() {
        System.out.print("Select a field (");
        Field[] fields = Contact.class.getDeclaredFields();
        for(int i = 0; i < fields.length; i++)
            System.out.printf(
                    "%s%s",
                    fields[i].getName(),
                    (i == fields.length - 1) ? "): " : ", "
            );

        String in = sc.nextLine();
        Field fieldInput = null;
        while (fieldInput == null) {
            for (Field field : fields)
                if(field.getName().equals(in.trim().toLowerCase()))
                    fieldInput = field;
            if(fieldInput == null) {
                System.out.println("Enter valid field!");
                in = sc.nextLine();
            }
        }

        System.out.printf("Enter %s: ", fieldInput.getName());
        String fieldValue = getStrInput();

        return new Object[]{fieldInput, fieldValue};
    }
}
