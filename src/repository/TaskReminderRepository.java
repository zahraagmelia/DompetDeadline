
package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TaskReminderRepository {

    // Memeriksa apakah pengingat sudah pernah ditampilkan
    public boolean sudahDiingatkan(
            int taskId,
            String deadline,
            String jenisPengingat
    ) throws SQLException {

        String sql = """
            SELECT COUNT(*) AS jumlah
            FROM task_reminders
            WHERE task_id = ?
            AND deadline = ?
            AND reminder_type = ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);
            statement.setString(2, deadline);
            statement.setString(3, jenisPengingat);

            try (ResultSet result = statement.executeQuery()) {
                return result.getInt("jumlah") > 0;
            }
        }
    }

    // Menyimpan riwayat pengingat
    public void simpanPengingat(
            int taskId,
            String deadline,
            String jenisPengingat
    ) throws SQLException {

        String sql = """
            INSERT OR IGNORE INTO task_reminders
            (task_id, deadline, reminder_type, notified_at)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);
            statement.setString(2, deadline);
            statement.setString(3, jenisPengingat);
            statement.setString(
                    4, LocalDateTime.now().toString()
            );

            statement.executeUpdate();
        }
    }
}
