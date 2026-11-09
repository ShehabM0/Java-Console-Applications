package carsharing;

import java.util.List;

interface CompanyDao {
    List<Company> findAll();
    void create(String companyName);
}
