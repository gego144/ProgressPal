package com.example.progresspal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progresspal.ArchivedTaskList;
import com.example.progresspal.Model.ArchivedTask;
import com.example.progresspal.R;

import java.util.ArrayList;

public class ArchivedTaskListAdapter extends RecyclerView.Adapter<ArchivedTaskListAdapter.ArchivedTaskListViewHolder> {

    static Context context;
    static ArrayList<ArchivedTask> list;

    public ArchivedTaskListAdapter(Context context, ArrayList<ArchivedTask> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ArchivedTaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.to_do_list, parent, false);
        return new ArchivedTaskListViewHolder(v).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivedTaskListViewHolder holder, int position) {
        ArchivedTask task = list.get(position);
        holder.title.setText(task.getTitle());
        holder.isCompleted.setChecked(task.getCompleted());
        if (task.getCompleted()) {
            holder.title.setTextColor(Color.parseColor("#B2B7BB"));
        } else {
            holder.title.setTextColor(Color.parseColor("#383F50"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ArchivedTaskListViewHolder extends RecyclerView.ViewHolder {

        ArchivedTaskListAdapter adapter;
        TextView title;
        CheckBox isCompleted;

        public ArchivedTaskListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            isCompleted = itemView.findViewById(R.id.taskCompletedCheckbox);
            isCompleted.setClickable(false);
        }

        public ArchivedTaskListViewHolder linkAdapter(ArchivedTaskListAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

    }
}
