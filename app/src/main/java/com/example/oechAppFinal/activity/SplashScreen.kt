package com.example.oechAppFinal.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.Constances
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (getSavedThemeState()) setTheme(R.style.Theme_OechAppFinal_Night)
        else setTheme(R.style.Theme_OechAppFinal)

        val delayMillis = 2000L
        val handler = Handler()
        var sessionExists: Boolean
        handler.postDelayed({
            lifecycleScope.launch(Dispatchers.IO) {
                val session = supabase.auth.sessionManager.loadSession()
                sessionExists = session != null
                Log.d("session", sessionExists.toString())
                if (sessionExists){
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashScreen, OnboardingScreen1::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }, delayMillis)
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://cvrihxuwwzuqfxulrjne.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN2cmloeHV3d3p1cWZ4dWxyam5lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkxNDM2MDEsImV4cCI6MjAyNDcxOTYwMX0.tBvqGRNPAmzW3BLfI-6KRUxPREzEsbgatzROVBcLDJY"
    ) {
        install(Auth)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}