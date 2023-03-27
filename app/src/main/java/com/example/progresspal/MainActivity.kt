package com.example.progresspal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.databinding.ActivityMainBinding
import java.sql.Date
import java.sql.Time

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: TaskAdapter;
    private lateinit var list: ArrayList<Task>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.toDoList
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        list = ArrayList<Task>()

        val date = Date.valueOf("2023-03-27")
        val time = Time.valueOf("02:31:30")

        val first = Task("1", "Finish the assignment \uD83D\uDE80", "High", date,time, false );
        val second = Task("2", "schedule a zoom call with design team", "High", date,time, true );
        val third = Task("3", "Finish up the team meeting", "High", date,time, false );
        val fourth = Task("4", "Program the code", "High", date,time, false );
        val fifth = Task("5", "reply to emails", "High", date,time, false );

        list.add(first)
        list.add(second)
        list.add(third)
        list.add(fourth)
        list.add(fifth)
        list.add(second)
        list.add(first)
        list.add(third)
        list.add(fourth)
        list.add(second)
        list.add(third)
        list.add(fourth)

        binding.progressBar.secondaryProgress = 25

        adapter =TaskAdapter(this, list);
        recyclerView.adapter = adapter

    }
}