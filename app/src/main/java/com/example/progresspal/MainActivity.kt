package com.example.progresspal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.databinding.ActivityMainBinding
import com.example.progresspal.persistence.TaskPersistence
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: TaskAdapter;
    private lateinit var list: ArrayList<Task>
    private lateinit var mModel: TaskPersistence

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.toDoList
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        list = ArrayList<Task>()

        //list = TaskPersistence.get()

        val date = Date.valueOf("2023-03-27")
        val time = Time.valueOf("02:31:00")

        val first = Task(1,"Finish the assignment \uD83D\uDE80", "High", date,time, "Daily",false );
        val second = Task(2,"schedule a zoom call with design team", "High", date,time, "Monthly",true );
        val third = Task(3,"Finish up the team meeting", "High", date,time, "Yearly",false );
        val fourth = Task(4,"Program the code", "High", date,time, "None",false );
        val fifth = Task(5,"reply to emails", "High", date,time, "None", false );

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

        findViewById<Button>(R.id.addTaskBtn).setOnClickListener {
            val intent = Intent(this, addTask::class.java)
            startActivity(intent)
        }
    }

}