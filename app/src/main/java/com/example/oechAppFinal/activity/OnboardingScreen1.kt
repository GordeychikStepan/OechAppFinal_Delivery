package com.example.oechAppFinal.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.Constances

class OnboardingScreen1 : AppCompatActivity() {
    private lateinit var skipButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen1)

        if(getSavedThemeState())
        {
            setTheme(R.style.Theme_OechAppFinal_Night)
        }
        else
        {
            setTheme(R.style.Theme_OechAppFinal)
        }

        skipButton = findViewById(R.id.skipButton)
        nextButton = findViewById(R.id.nextButton)

        nextButton.setOnClickListener {
            val intent = Intent(this, OnboardingScreen2::class.java)
            startActivity(intent)
            finish()
        }

        skipButton.setOnClickListener {
            val intent = Intent(this, OnboardingScreen3::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}