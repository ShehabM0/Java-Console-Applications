package carsharing;

import com.beust.jcommander.JCommander;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] argv) {
        Args args = new Args();
        JCommander.newBuilder().addObject(args).build().parse(argv);

        String dbURL = "jdbc:h2:./src/carsharing/db/" + args.dbURL;
        try(Connection conn = Database.getConnection(dbURL)) {
            Database.createCompanyTable(conn);

            CompanyDao companyDao = new CompanyDaoImpl(conn);

            mainMenu(companyDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sc.close();
    }

    private static void mainMenu(CompanyDao companyDao) {
        while(true) {
            System.out.print("""
                    1. Log in as a manager
                    0. Exit
                    """);
            String menuIdx = sc.nextLine();
            switch (menuIdx) {
                case "1" -> managerMenu(companyDao);
                case "0" -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void managerMenu(CompanyDao companyDao) {
        while (true) {
            System.out.print("""
                \n1. Company list
                2. Create a company
                0. Back
                """);
            String menuIdx = sc.nextLine();
            switch (menuIdx) {
                case "1" -> listCompanyHandler(companyDao);
                case "2" -> createCompanyHandler(companyDao);
                case "0" -> {
                    System.out.println();
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void listCompanyHandler(CompanyDao companyDao) {
        List<Company> companies = companyDao.findAll();
        if(companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        companies.forEach(System.out::println);
    }

    private static void createCompanyHandler(CompanyDao companyDao) {
        System.out.println("\nEnter the company name:");
        String companyName = sc.nextLine();
        companyDao.create(companyName);
        System.out.println("The company was created!");
    }
}
