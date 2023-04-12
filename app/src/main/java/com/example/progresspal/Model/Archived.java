package com.example.progresspal.Model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Archived {
    Timestamp date;
    int progress;
    ArrayList<ArchivedTask> tasks;

    public Archived(Timestamp date, int progress, ArrayList<ArchivedTask> list) {
        this.date = date;
        this.progress = progress;
        this.tasks = list;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public ArrayList<ArchivedTask> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<ArchivedTask> tasks) {
        this.tasks = tasks;
    }

}
