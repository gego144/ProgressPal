package com.example.progresspal.Model;


import com.google.firebase.Timestamp;
import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.HashMap;

public class Archived {
    Timestamp date;
    int progress;

    ArrayList<HashMap<String, Any>> tasks;

    public Archived(Timestamp date, int progress, ArrayList<HashMap<String, Any>> tasks) {
        this.date = date;
        this.progress = progress;
        this.tasks = tasks;
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

    public ArrayList<HashMap<String, Any>> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<HashMap<String, Any>> tasks) {
        this.tasks = tasks;
    }
}
