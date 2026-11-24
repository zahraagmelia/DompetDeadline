
package model;

import java.time.LocalDate;

public class Task {

    private int id;
    private String title;
    private String course;
    private LocalDate deadline;
    private String priority;
    private String status;
    private int estimatedCost;

    public Task(int id, String title, String course,
                LocalDate deadline, String priority,
                String status, int estimatedCost) {
        this.id = id;
        this.title = title;
        this.course = course;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        this.estimatedCost = estimatedCost;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public int getEstimatedCost() {
        return estimatedCost;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
