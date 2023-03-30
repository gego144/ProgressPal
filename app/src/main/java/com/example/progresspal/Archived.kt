package com.example.progresspal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.progresspal.databinding.ActivityArchivedBinding
import com.example.progresspal.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class Archived : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityArchivedBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchivedBinding.inflate(layoutInflater)
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