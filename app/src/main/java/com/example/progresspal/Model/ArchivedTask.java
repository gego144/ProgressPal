package com.example.progresspal.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class ArchivedTask implements Parcelable {
    String title;
    Boolean isCompleted;

    public ArchivedTask(String title, Boolean isCompleted) {
        this.title = title;
        this.isCompleted = isCompleted;
    }

    protected ArchivedTask(Parcel in) {
        title = in.readString();
        byte tmpIsCompleted = in.readByte();
        isCompleted = tmpIsCompleted == 0 ? null : tmpIsCompleted == 1;
    }

    public static final Parcelable.Creator<ArchivedTask> CREATOR = new Parcelable.Creator<ArchivedTask>() {
        @Override
        public ArchivedTask createFromParcel(Parcel in) {
            return new ArchivedTask(in);
        }

        @Override
        public ArchivedTask[] newArray(int size) {
            return new ArchivedTask[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        parcel.writeString(title);
        parcel.writeByte((byte) (isCompleted == null ? 0 : isCompleted ? 1 : 2));
    }
}
