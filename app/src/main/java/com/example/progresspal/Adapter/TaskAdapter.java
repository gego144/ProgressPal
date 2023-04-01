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

import com.example.progresspal.Model.Task;
import com.example.progresspal.R;
import com.example.progresspal.editTask;
import com.example.progresspal.persistence.TaskPersistence;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    static Context context;
    static ArrayList<Task> list;

    public TaskAdapter(Context context, ArrayList<Task> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.to_do_list, parent, false);
        return new TaskViewHolder(v).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = list.get(position);
        holder.title.setText(task.getTitle());
        holder.isCompleted.setChecked(task.getCompleted());
        if(task.getCompleted()) {
            holder.title.setTextColor(Color.parseColor("#B2B7BB"));
        } else {
            holder.title.setTextColor(Color.parseColor("#383F50"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TaskAdapter adapter;
        TextView title;
        CheckBox isCompleted;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            isCompleted = itemView.findViewById(R.id.taskCompletedCheckbox);
            isCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task task = list.get(getAdapterPosition());
                    TaskPersistence.edit(task, getAdapterPosition(), true);
                    if(isCompleted.isChecked()) {
                        adapter.list.get(getAdapterPosition()).setCompleted(true);
                        adapter.notifyItemChanged(getAdapterPosition());
                    } else {
                        adapter.list.get(getAdapterPosition()).setCompleted(false);
                        adapter.notifyItemChanged(getAdapterPosition());
                    }
                }
            });

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task task = list.get(getAdapterPosition());
                    System.out.println(context.getClass().getName());
                    if(context.getClass().getName().equals("com.example.progresspal.MainActivity")){
                        Intent intent = new Intent(context, editTask.class);
                        intent.putExtra("task", task);
                        intent.putExtra("pos", getAdapterPosition());
                        context.startActivity(intent);
                    }
                }
            });
        }

        public TaskViewHolder linkAdapter(TaskAdapter adapter) {
            this.adapter = adapter;
            return this;
        }


    }
}
