package com.example.oechAppFinal.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.Constances
import com.google.android.material.snackbar.Snackbar
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setTheme(R.style.Theme_OechAppFinal)

        val emailAddressEditText = findViewById<TextView>(R.id.emailAddressEditText)
        val sendOtpButton = findViewById<Button>(R.id.sendOtpButton)
        val signInTextView = findViewById<TextView>(R.id.signInTextView)

        signInTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        emailAddressEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val emailAddress: String = emailAddressEditText.text.toString().trim()
                val isValidEmailAddress =
                    android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
                if (!isValidEmailAddress) {
                    emailAddressEditText.error = "Enter a valid email address."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getOtpButtonAccess(
                    emailAddressEditText,
                    sendOtpButton
                )
            }
        })

        sendOtpButton.setOnClickListener {
            if (emailAddressEditText.text.toString().isNotEmpty()) {
                val email = emailAddressEditText.text.toString().lowercase()
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val user = supabase.auth.resetPasswordForEmail(email)
                        val intent = Intent(this@ForgotPasswordActivity, OtpVerificationActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                        finish()
                    } catch (error: Exception) {
                        Log.d("logInException", error.toString())
                        var snackbar =
                            Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                }
            } else {
                emailAddressEditText.error = "Empty"
            }
        }
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://cvrihxuwwzuqfxulrjne.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN2cmloeHV3d3p1cWZ4dWxyam5lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkxNDM2MDEsImV4cCI6MjAyNDcxOTYwMX0.tBvqGRNPAmzW3BLfI-6KRUxPREzEsbgatzROVBcLDJY"
    ) {
        install(Auth)
        install(Postgrest)
    }

    private fun getOtpButtonAccess(
        emailAddressEditText: TextView,
        sendOtpButton: Button,
    ) {
        val emailCheck =
            !emailAddressEditText.text.isNullOrEmpty() && emailAddressEditText.error.isNullOrEmpty()

        if (emailCheck) {
            sendOtpButton.isClickable = true
            val color = Color.parseColor("#0560FA")
            sendOtpButton.setBackgroundColor(color)
        } else {
            sendOtpButton.isClickable = false
            val color = Color.parseColor("#A7A7A7")
            sendOtpButton.setBackgroundColor(color)
        }
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}