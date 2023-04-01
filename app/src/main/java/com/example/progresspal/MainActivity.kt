package com.example.progresspal

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Adapter.TaskAdapter
import com.example.progresspal.Model.Task
import com.example.progresspal.databinding.ActivityMainBinding
import com.example.progresspal.persistence.TaskPersistence
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: TaskAdapter;
    private lateinit var list: ArrayList<Task>

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.toDoList
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.progressBar.secondaryProgress = 25
        list = TaskPersistence.get(recyclerView)

        adapter = TaskAdapter(this, list);
        recyclerView.adapter = adapter

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        toolbar = binding.menuBtn

        setSupportActionBar(toolbar);
        supportActionBar!!.setIcon(R.drawable.menu)

        navigationView.bringToFront()
        val toggle: ActionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.home)

        findViewById<Button>(R.id.addTaskBtn).setOnClickListener {

            val intent = Intent(this, addTask::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {}
            R.id.archived -> {
                val intent = Intent(this, Archived::class.java)
                startActivity(intent)
            }
            R.id.stats -> {
                val intent = Intent(this, Stats::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}