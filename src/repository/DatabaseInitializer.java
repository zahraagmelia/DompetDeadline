
package repository;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initialize() {

        String createTasks = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                course TEXT NOT NULL,
                deadline TEXT NOT NULL,
                priority TEXT NOT NULL,
                status TEXT NOT NULL,
                estimated_cost INTEGER NOT NULL DEFAULT 0
            )
            """;

        String createCategories = """
            CREATE TABLE IF NOT EXISTS categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL
            )
            """;

        String createTransactions = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                date TEXT NOT NULL,
                amount INTEGER NOT NULL,
                category TEXT NOT NULL,
                note TEXT,
                task_id INTEGER,
                FOREIGN KEY (task_id) REFERENCES tasks(id)
            )
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTasks);
            statement.execute(createCategories);
            statement.execute(createTransactions);

            System.out.println("Database berhasil dibuat!");

        } catch (SQLException e) {
            System.out.println("Gagal membuat database: "
                    + e.getMessage());
        }
    }
}
