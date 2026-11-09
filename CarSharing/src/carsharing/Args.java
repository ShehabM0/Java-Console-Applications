package carsharing;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "-databaseFileName", description = "Database URL")
    String dbURL = "test";
}
