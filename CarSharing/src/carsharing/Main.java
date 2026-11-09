package carsharing;

import com.beust.jcommander.JCommander;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    // private static final String URL = "jdbc:h2:~/test";
    public static void main(String[] argv) {
        Args args = new Args();
        JCommander.newBuilder()
                .addObject(args)
                .build()
                .parse(argv);

        String URL = "jdbc:h2:./src/carsharing/db/" + args.dbURL;

        try(Connection conn = DriverManager.getConnection(URL)) {
            System.out.println("Connected to H2!");
            String sql = """
                    CREATE TABLE IF NOT EXISTS COMPANY (
                        id INT,
                        name VARCHAR(100)
                    );
                    """;
            try(Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}