package com.example.progresspal.Model;


import com.google.firebase.Timestamp;

public class Archived {
    String date;
    String progress;

    public Archived(String date, String progress) {
        this.date = date;
        this.progress = progress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
