package com.example.progresspal

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.progresspal.databinding.ActivityStatsBinding
import com.example.progresspal.persistence.ArchivePersistence
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import java.io.File
import java.io.FileOutputStream
import java.sql.Date

class Stats : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityStatsBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val maxStreak = 15
        val currentStreak = 14
        binding.streakCount.text = currentStreak.toString()
        if (currentStreak < maxStreak) {
            binding.streakDescription.text = "Your highest streak is $maxStreak. Let's beat that."
        } else {
            binding.streakDescription.text = "This is you highest streak.Keep it up."
        }

        ArchivePersistence.getStats(binding)

        binding.shareBtn.setOnClickListener {

            val b: Bitmap = Screenshot.takeScreenshotOfView(binding.layoutForScreenShot)

            val file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out: FileOutputStream = FileOutputStream(file)
            b.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            b.recycle()
            val imageUri: Uri = FileProvider.getUriForFile(
                this,
                "com.example.progresspal.provider", //(use your app signature + ".provider" )
                file)

            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/*"
            }
            startActivity(shareIntent, null)
        }

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
        navigationView.setCheckedItem(R.id.stats)
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
            R.id.stats -> {}
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    companion object Screenshot {
        private fun takeScreenshot(view: View): Bitmap {
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache(true)
            val b = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            return b
        }

        fun takeScreenshotOfView(v: View): Bitmap {
            return takeScreenshot(v)
        }
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}