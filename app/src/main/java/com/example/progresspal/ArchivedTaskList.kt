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
import com.example.progresspal.Adapter.ArchivedAdapter
import com.example.progresspal.Adapter.ArchivedTaskListAdapter
import com.example.progresspal.Model.ArchivedTask
import com.example.progresspal.databinding.ActivityArchivedTaskListBinding
import com.example.progresspal.persistence.ArchivePersistence
import com.google.android.material.navigation.NavigationView

class ArchivedTaskList : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityArchivedTaskListBinding
    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: ArchivedTaskListAdapter;
    private lateinit var list: ArrayList<ArchivedTask>

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchivedTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.toDoList
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        list = ArrayList<ArchivedTask>()

        val position = getIntent().getIntExtra("position", 0)
        val date = getIntent().getStringExtra("date")
        val progress = getIntent().getIntExtra("progress", 0)
        val tasks: ArrayList<ArchivedTask>? =
            this.intent.getParcelableArrayListExtra<ArchivedTask>("tasks")

        if (tasks != null) {
            list = tasks
        }

        binding.progressBar.secondaryProgress = progress
        binding.archivedTaskDate.text = "Check your progress $date"
        adapter = ArchivedTaskListAdapter(this, list);
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

        findViewById<Button>(R.id.archivedTaskDeleteBtn).setOnClickListener {
            val intent = Intent(this, Archived::class.java)
            ArchivePersistence.delete(position)
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
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
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