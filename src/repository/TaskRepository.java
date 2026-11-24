
package repository;

import model.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    public void addTask(Task task) throws SQLException {
        String sql = """
            INSERT INTO tasks
            (title, course, deadline, priority, status, estimated_cost)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, task.getTitle());
            statement.setString(2, task.getCourse());
            statement.setString(3, task.getDeadline().toString());
            statement.setString(4, task.getPriority());
            statement.setString(5, task.getStatus());
            statement.setInt(6, task.getEstimatedCost());

            statement.executeUpdate();
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY deadline ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Task task = new Task(
                    result.getInt("id"),
                    result.getString("title"),
                    result.getString("course"),
                    java.time.LocalDate.parse(result.getString("deadline")),
                    result.getString("priority"),
                    result.getString("status"),
                    result.getInt("estimated_cost")
                );

                tasks.add(task);
            }
        }

        return tasks;
    }

    public void updateTask(Task task) throws SQLException {
        String sql = """
            UPDATE tasks
            SET title = ?, course = ?, deadline = ?,
                priority = ?, status = ?, estimated_cost = ?
            WHERE id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, task.getTitle());
            statement.setString(2, task.getCourse());
            statement.setString(3, task.getDeadline().toString());
            statement.setString(4, task.getPriority());
            statement.setString(5, task.getStatus());
            statement.setInt(6, task.getEstimatedCost());
            statement.setInt(7, task.getId());

            statement.executeUpdate();
        }
    }

    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
