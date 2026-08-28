package readability;

import java.io.File;
import java.util.*;

public class Main {
    private static File createFile(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide file name!");
            System.exit(1);
        }

        String fileName = args[0];
        File file = new File(fileName);
        if(!file.isFile()) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
        }
        return file;
    }

    public static void main(String[] args) {
        final File file = createFile(args);
        try (Scanner sc = new Scanner(System.in)){
            new FileAnalyzer(file, sc).analyze();
        }
    }
}
