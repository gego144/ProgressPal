package com.example.progresspal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progresspal.ArchivedTaskList;
import com.example.progresspal.Model.Archived;
import com.example.progresspal.R;

import android.text.format.DateFormat;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
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
        Date date = new Date(archived.getDate().getSeconds() * 1000);
        Format dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = dateFormat.format(date);
        holder.date.setText(dateString);
        holder.progress.setText(archived.getProgress() + "%");
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
            LinearLayout layout = itemView.findViewById(R.id.archivedListLayout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ArchivedTaskList.class);

                    Archived archived = list.get(getAdapterPosition());
                    Date date = new Date(archived.getDate().getSeconds() * 1000);
                    Format dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                    String dateString = dateFormat.format(date);
                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("date", dateString);
                    intent.putExtra("progress", archived.getProgress());
                    intent.putParcelableArrayListExtra("tasks", archived.getTasks());
                    context.startActivity(intent);
                }
            });
        }

        public ArchivedViewHolder linkAdapter(ArchivedAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

    }
}
