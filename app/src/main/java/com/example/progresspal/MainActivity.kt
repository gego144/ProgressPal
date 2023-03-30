package com.example.progresspal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.databinding.ActivityMainBinding
import com.example.progresspal.persistence.TaskPersistence

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

        binding.progressBar.secondaryProgress = 25
        list = TaskPersistence.get(recyclerView)

        adapter =TaskAdapter(this, list);
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.addTaskBtn).setOnClickListener {

            val intent = Intent(this, addTask::class.java)
            startActivity(intent)
        }
    }

}