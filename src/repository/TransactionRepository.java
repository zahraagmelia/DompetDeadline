
package repository;

import model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.YearMonth;

public class TransactionRepository {

    public void addTransaction(Transaction transaction)
            throws SQLException {

        String sql = """
            INSERT INTO transactions
            (type, date, amount, category, note, task_id)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transaction.getType());
            statement.setString(2, transaction.getDate().toString());
            statement.setInt(3, transaction.getAmount());
            statement.setString(4, transaction.getCategory());
            statement.setString(5, transaction.getNote());

            if (transaction.getTaskId() == null) {
                statement.setNull(6, Types.INTEGER);
            } else {
                statement.setInt(6, transaction.getTaskId());
            }

            statement.executeUpdate();
        }
    }

    public List<Transaction> getAllTransactions()
            throws SQLException {

        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY date DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                int taskIdValue = result.getInt("task_id");
                Integer taskId = result.wasNull() ? null : taskIdValue;

                Transaction transaction = new Transaction(
                    result.getInt("id"),
                    result.getString("type"),
                    java.time.LocalDate.parse(result.getString("date")),
                    result.getInt("amount"),
                    result.getString("category"),
                    result.getString("note"),
                    taskId
                );

                transactions.add(transaction);
            }
        }

        return transactions;
    }

    // Mengubah transaksi
    public void updateTransaction(Transaction transaction)
            throws SQLException {

        String sql = """
            UPDATE transactions
            SET type = ?, date = ?, amount = ?,
                category = ?, note = ?, task_id = ?
            WHERE id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transaction.getType());
            statement.setString(2, transaction.getDate().toString());
            statement.setInt(3, transaction.getAmount());
            statement.setString(4, transaction.getCategory());
            statement.setString(5, transaction.getNote());

            if (transaction.getTaskId() == null) {
                statement.setNull(6, Types.INTEGER);
            } else {
                statement.setInt(6, transaction.getTaskId());
            }

            statement.setInt(7, transaction.getId());
            statement.executeUpdate();
        }
    }

    // Menghapus transaksi
    public void deleteTransaction(int id)
            throws SQLException {

        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // Menghitung saldo bulan ini
    public int getMonthlyBalance() throws SQLException {

        String sql = """
            SELECT COALESCE(
                SUM(
                    CASE
                        WHEN type = 'Pemasukan' THEN amount
                        WHEN type = 'Pengeluaran' THEN -amount
                        ELSE 0
                    END
                ), 0
            ) AS balance
            FROM transactions
            WHERE substr(date, 1, 7) = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, YearMonth.now().toString());

            try (ResultSet result = statement.executeQuery()) {
                return result.getInt("balance");
            }
        }
    }
}
