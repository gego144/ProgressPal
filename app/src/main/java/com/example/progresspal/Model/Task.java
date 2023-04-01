package com.example.progresspal.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Task implements Parcelable {
    int id;
    String title;
    String priority;
    Timestamp dueDate;
    Timestamp reminder;
    String repeat;
    Boolean isCompleted;

    public Task(int id, String title, String priority, Timestamp dueDate, Timestamp reminder, String repeat, Boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.dueDate = dueDate;
        this.reminder = reminder;
        this.repeat = repeat;
        this.isCompleted = isCompleted;
    }

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        priority = in.readString();
        dueDate = in.readParcelable(Timestamp.class.getClassLoader());
        reminder = in.readParcelable(Timestamp.class.getClassLoader());
        repeat = in.readString();
        byte tmpIsCompleted = in.readByte();
        isCompleted = tmpIsCompleted == 0 ? null : tmpIsCompleted == 1;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

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

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Timestamp getReminder() {
        return reminder;
    }

    public void setReminder(Timestamp reminder) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(priority);
        parcel.writeParcelable(dueDate, i);
        parcel.writeParcelable(reminder, i);
        parcel.writeString(repeat);
        parcel.writeByte((byte) (isCompleted == null ? 0 : isCompleted ? 1 : 2));
    }
}
