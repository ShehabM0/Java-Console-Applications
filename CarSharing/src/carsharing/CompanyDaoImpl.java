package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class CompanyDaoImpl implements CompanyDao {
    private final Connection conn;

    CompanyDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Company> findAll() {
        List<Company> companies = new ArrayList<>();

        String q = """
                SELECT *
                FROM Company
                ORDER BY id
                """;
        try(
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(q)
        ) {
            while (resultSet.next()) {
                companies.add(new Company(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return companies;
    }

    @Override
    public void create(String companyName) {
        String q = """
                INSERT INTO Company (name)
                VALUES (?)
                """;
        try(PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, companyName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
