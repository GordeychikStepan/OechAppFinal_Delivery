package com.example.oechAppFinal.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.Constances

class OnboardingScreen3 : AppCompatActivity() {
    private lateinit var signUpButton: Button
    private lateinit var signInTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen3)

        if(getSavedThemeState())
        {
            setTheme(R.style.Theme_OechAppFinal_Night)
        }
        else
        {
            setTheme(R.style.Theme_OechAppFinal)
        }

        signUpButton = findViewById(R.id.signUpButton)
        signInTextView = findViewById(R.id.signInTextView)

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        signInTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
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