package com.example.progresspal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progresspal.Model.Archived;
import com.example.progresspal.Model.Task;
import com.example.progresspal.R;
import com.example.progresspal.editTask;

import java.util.ArrayList;

public class ArchivedAdapter extends RecyclerView.Adapter<ArchivedAdapter.ArchivedViewHolder> {

    static Context context;
    static ArrayList<Archived> list;

    public ArchivedAdapter(Context context, ArrayList<Archived> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ArchivedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.archived_list, parent, false);
        return new ArchivedViewHolder(v).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivedViewHolder holder, int position) {
        Archived archived = list.get(position);
        holder.date.setText(archived.getDate());
        holder.progress.setText(archived.getProgress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ArchivedViewHolder extends RecyclerView.ViewHolder {

        ArchivedAdapter adapter;
        TextView date;
        TextView progress;

        public ArchivedViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.archivedDate);
            progress = itemView.findViewById(R.id.archivedProgress);
        }

        public ArchivedViewHolder linkAdapter(ArchivedAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

    }
}
