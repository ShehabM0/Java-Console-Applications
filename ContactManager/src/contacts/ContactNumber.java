package contacts;

class ContactNumber {
    public static boolean validateNumber(String[] strSegments) {
        int n = strSegments.length;
        if (n == 0)
            return false;

        boolean validateGroups = validateFirstGroup(strSegments[0]);
        if (n > 1)
            validateGroups = validateGroups && validateSecondGroup(strSegments[1]);

        for (int i = 2; i < strSegments.length; i++)
            validateGroups = validateGroups && validateGroup(strSegments[i]);

        if(n > 1)
            return validateGroups &&
                    !(groupContainsParentheses(strSegments[0]) &&
                            groupContainsParentheses(strSegments[1])
                    );
        else
            return validateGroups;
    }

    private static boolean validateChar(char c) {
        return (c >= '0' && c <= '9') ||
                (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z');
    }

    private static boolean validateFirstGroup(String s) {
        int n = s.length();
        if (n == 0)
            return false;

        char firstChar = s.charAt(0);
        boolean validateFirstChar = firstChar == '(' || firstChar == '+' || validateChar(firstChar);
        if (!validateFirstChar)
            return false;

        int i = 0;
        if (firstChar == '+') {
            if (n < 2)
                return false;
            firstChar = s.charAt(1);
            i++;
        }
        if (firstChar == '(') {
            if (s.charAt(n - 1) != ')')
                return false;
            else if (n - 1 - i == 1)
                return false;
            i++;
            n--;
        }

        for (; i < n; i++)
            if (!validateChar(s.charAt(i)))
                return false;

        return true;
    }

    private static boolean validateSecondGroup(String s) {
        int n = s.length();
        if (n < 2)
            return false;

        char firstChar = s.charAt(0);
        boolean validateFirstChar = firstChar == '(' || validateChar(firstChar);
        if (!validateFirstChar)
            return false;

        int i = 0;
        if (firstChar == '(') {
            if (s.charAt(n - 1) != ')')
                return false;
            else if (n - 1 == 1)
                return false;
            i++;
            n--;
        }

        for (; i < n; i++)
            if (!validateChar(s.charAt(i)))
                return false;

        return true;
    }

    private static boolean validateGroup(String s) {
        int n = s.length();
        if (n < 2)
            return false;

        for (int i = 0; i < n; i++)
            if (!validateChar(s.charAt(i)))
                return false;

        return true;
    }

    private static boolean groupContainsParentheses(String s) {
        return s.contains("(");
    }
}
