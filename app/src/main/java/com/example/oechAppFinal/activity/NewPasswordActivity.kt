package com.example.oechAppFinal.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.Constances
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        setTheme(R.style.Theme_OechAppFinal)

        val passwordEditText = findViewById<TextView>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<TextView>(R.id.confirmPasswordEditText)
        val logInButton = findViewById<Button>(R.id.logInButton)
        val passwordVisibilityToggle1 = findViewById<ImageView>(R.id.passwordVisibilityToggle1)
        val passwordVisibilityToggle2 = findViewById<ImageView>(R.id.passwordVisibilityToggle2)

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password: String = passwordEditText.text.toString().trim()
                val passwordPattern =
                    Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
                val isValidPassword = password.matches(passwordPattern)
                if (!isValidPassword) {
                    passwordEditText.error =
                        "The password must contain at least 8 characters, including upper and lower case letters, numbers and special characters."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getChangePasswordButtonAccess(
                    passwordEditText,
                    confirmPasswordEditText,
                    logInButton
                )
            }
        })

        confirmPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password: String = passwordEditText.text.toString().trim()
                val confirmPassword: String = confirmPasswordEditText.text.toString().trim()
                if (password != confirmPassword) {
                    confirmPasswordEditText.error = "Password doesn't match."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getChangePasswordButtonAccess(
                    passwordEditText,
                    confirmPasswordEditText,
                    logInButton
                )
            }
        })

        logInButton.setOnClickListener {
            val password = passwordEditText.text.toString().trim()
            lifecycleScope.launch(Dispatchers.IO) {
                supabase.auth.modifyUser {
                    this.password = password
                }
                supabase.auth.sessionStatus.collect {
                    when (it) {
                        is SessionStatus.Authenticated -> {
                            // Обновления пользовательского интерфейса должны выполняться в основном потоке
                            lifecycleScope.launch(Dispatchers.Main) {
                                val intent = Intent(this@NewPasswordActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        else -> {
                            // Обновления пользовательского интерфейса должны выполняться в основном потоке
                            lifecycleScope.launch(Dispatchers.Main) {
                                Toast.makeText(
                                    this@NewPasswordActivity,
                                    "Упс! Что-то пошло не так",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        // обработчики нажатия на кнопку глаза
        passwordVisibilityToggle1.setOnClickListener {
            if (passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        passwordVisibilityToggle2.setOnClickListener {
            if (confirmPasswordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                confirmPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://cvrihxuwwzuqfxulrjne.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN2cmloeHV3d3p1cWZ4dWxyam5lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkxNDM2MDEsImV4cCI6MjAyNDcxOTYwMX0.tBvqGRNPAmzW3BLfI-6KRUxPREzEsbgatzROVBcLDJY"
    ) {
        install(Auth)
    }

    private fun getChangePasswordButtonAccess(passwordEditText: TextView, confirmPasswordEditText: TextView, logInButton: Button) {
        val passwordCheck =
            !passwordEditText.text.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()
        val confirmPasswordCheck =
            !confirmPasswordEditText.text.isNullOrEmpty() && confirmPasswordEditText.error.isNullOrEmpty()

        if (passwordCheck && confirmPasswordCheck) {
            logInButton.isClickable = true
            val color = Color.parseColor("#0560FA")
            logInButton.setBackgroundColor(color)
        } else {
            logInButton.isClickable = false
            val color = Color.parseColor("#A7A7A7")
            logInButton.setBackgroundColor(color)
        }
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}