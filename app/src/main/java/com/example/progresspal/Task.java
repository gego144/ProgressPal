package com.example.progresspal;

import java.sql.Date;
import java.sql.Time;

public class Task {
    int id;
    String title;
    String priority;
    Date dueDate;
    Time reminder;
    String repeat;
    Boolean isCompleted;

    public Task(int id, String title, String priority, Date dueDate, Time reminder, String repeat, Boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.dueDate = dueDate;
        this.reminder = reminder;
        this.repeat = repeat;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Time getReminder() {
        return reminder;
    }

    public void setReminder(Time reminder) {
        this.reminder = reminder;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getRepeat(){
        return repeat;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }
}
