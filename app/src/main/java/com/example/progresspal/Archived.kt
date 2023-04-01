package com.example.progresspal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progresspal.Adapter.ArchivedAdapter
import com.example.progresspal.Adapter.TaskAdapter
import com.example.progresspal.Model.Archived
import com.example.progresspal.Model.ArchivedTask
import com.example.progresspal.Model.Task
import com.example.progresspal.databinding.ActivityArchivedBinding
import com.example.progresspal.databinding.ActivityMainBinding
import com.example.progresspal.persistence.TaskPersistence
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import java.sql.Date
import java.sql.Time

class Archived : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityArchivedBinding
    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: ArchivedAdapter;
    private lateinit var list: ArrayList<Archived>

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchivedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.archivedList
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

//        list = TaskPersistence.get(recyclerView)
        list = ArrayList<Archived>()
        val taskList: ArrayList<ArchivedTask> = ArrayList()
        taskList.add(ArchivedTask("task1", false))
        taskList.add(ArchivedTask("task2", true))
        taskList.add(ArchivedTask("task3", true))
        taskList.add(ArchivedTask("task4", false))
        val timestamp1 = Timestamp(Date.valueOf("2023-03-22"))
        val timestamp2 = Timestamp(Date.valueOf("2023-03-21"))
        val timestamp3 = Timestamp(Date.valueOf("2023-03-20"))
        val timestamp4 = Timestamp(Date.valueOf("2023-03-19"))
        val arch1 = Archived(timestamp1, 75, taskList)
        val arch2 = Archived(timestamp2, 95, taskList)
        val arch3 = Archived(timestamp3, 80, taskList)
        val arch4 = Archived(timestamp4, 55, taskList)
        list.add(arch1)
        list.add(arch2)
        list.add(arch3)
        list.add(arch4)

        adapter = ArchivedAdapter(this, list);
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
        navigationView.setCheckedItem(R.id.archived)
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
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.archived -> {}
            R.id.stats -> {
                val intent = Intent(this, Stats::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}