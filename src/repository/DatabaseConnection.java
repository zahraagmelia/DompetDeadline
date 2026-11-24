
package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatabaseConnection {

    private static final String DB_FOLDER =
            System.getProperty("user.home") + "/DompetDeadline";

    private static final String DB_URL =
            "jdbc:sqlite:" + DB_FOLDER + "/dompet_deadline.db";

    public static Connection getConnection() throws SQLException {
        try {
            Files.createDirectories(Path.of(DB_FOLDER));
        } catch (Exception e) {
            throw new SQLException("Gagal membuat folder database.", e);
        }

        return DriverManager.getConnection(DB_URL);
    }
}
