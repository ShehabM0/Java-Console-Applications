package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class Database {
    static Connection getConnection(String URL) throws SQLException {
        return DriverManager.getConnection(URL);
    }

    static void createCompanyTable(Connection conn) {
        String sql = """
            CREATE TABLE IF NOT EXISTS Company (
                id INT AUTO_INCREMENT,
                name VARCHAR(100) NOT NULL,
                PRIMARY KEY(id),
                UNIQUE(name)
            );
            """;
        try(Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table created.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void dropCompanyTable(Connection conn) {
        String sql = "DROP TABLE IF EXISTS Company";
        try(Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table deleted.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
