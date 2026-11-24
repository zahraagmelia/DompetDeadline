
package model;

import java.time.LocalDate;

public class Transaction {

    private int id;
    private String type;
    private LocalDate date;
    private int amount;
    private String category;
    private String note;
    private Integer taskId;

    public Transaction(int id, String type, LocalDate date,
                       int amount, String category,
                       String note, Integer taskId) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.note = note;
        this.taskId = taskId;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    public Integer getTaskId() {
        return taskId;
    }
}
