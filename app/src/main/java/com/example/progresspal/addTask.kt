package com.example.progresspal

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.progresspal.Model.Task
import com.example.progresspal.databinding.ActivityAddTaskBinding
import com.example.progresspal.persistence.TaskPersistence
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import java.sql.Time

/**
 * * Created by David Adane on 27/03/2023
 */

class addTask : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        val id: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


        // priorities code
        var priority = "High"
        val priorities = arrayOf("High", "Medium", "Low")
        val spinner = binding.prioritySpinner
        val priorityAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, priorities)
        spinner.adapter = priorityAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                priority = priorities[p2]
                println(priorities[p2])
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // time picker code
        val timePickerOpen = binding.timePickerOpen
        var pickedTime = ""

        timePickerOpen.setOnClickListener() {
            val timePickerDialog = TimePickerDialog(
                this,
                { timePicker: TimePicker, i: Int, i1: Int -> //Displaying the picked value in the textView
                    pickedTime = "$i:$i1"
                    timePickerOpen.text = pickedTime
                },
                12,
                0,
                true
            )

            timePickerDialog.show()
        }

        // repeat code
        var repeatWhen = "None"
        val repeat = arrayOf("None", "Daily", "Weekly", "Monthly")
        val repeatSpinner = binding.repeatSpinner
        val repeatAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, repeat)
        repeatSpinner.adapter = repeatAdapter
        repeatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                repeatWhen = repeat[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        val intent = Intent(this, MainActivity::class.java)

        val taskName = binding.taskName.text;

        binding.addTaskSaveChangesBtn.setOnClickListener {
            var date: Timestamp = Timestamp.now()
            var time: Timestamp
            // The if statement was to check if the user selected a time. If not it would default to noon
            if (pickedTime == "") {
                time = Timestamp(Time.valueOf("12:00:00"))
            } else {
                time = Timestamp(Time.valueOf(pickedTime + ":00"))
            }

            val first = Task(
                0,
                "$taskName",
                priority,
                date,
                time,
                repeatWhen,
                false
            );
            TaskPersistence.create(first, id)
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