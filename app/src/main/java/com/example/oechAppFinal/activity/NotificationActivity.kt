package com.example.oechAppFinal.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.oechAppFinal.R
import com.example.oechAppFinal.databinding.ActivityNotificationBinding
import com.example.oechAppFinal.model.Constances

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notification"

        if (getSavedThemeState())
        {
            binding.layout.setBackgroundColor(resources.getColor(R.color.blue_primary_shade1, theme))
            binding.bell.imageTintList = resources.getColorStateList(R.color.white, theme)
            binding.textView.setTextColor(resources.getColor(R.color.white, theme))
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}