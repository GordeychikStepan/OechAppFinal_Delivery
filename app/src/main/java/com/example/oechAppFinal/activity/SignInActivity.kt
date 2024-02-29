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
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.Constances
import com.google.android.material.snackbar.Snackbar
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setTheme(R.style.Theme_OechAppFinal)

        val forgotPasswordTextView = findViewById<TextView>(R.id.forgotPasswordTextView)
        val emailAddressEditText = findViewById<TextView>(R.id.emailAddressEditText)
        val passwordEditText = findViewById<TextView>(R.id.passwordEditText)
        val logInButton = findViewById<Button>(R.id.logInButton)
        val signUnTextView = findViewById<TextView>(R.id.signUnTextView)
        val passwordVisibilityToggle = findViewById<ImageView>(R.id.passwordVisibilityToggle)
        val signInGoogleImage = findViewById<ImageView>(R.id.signInGoogleImage)
        val rememberPasswordChkBx = findViewById<CheckBox>(R.id.rememberPassword)
        val rootView = findViewById<View>(android.R.id.content)

        passwordVisibilityToggle.setOnClickListener {
            if (passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        emailAddressEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val emailAddress: String = emailAddressEditText.text.toString().trim()
                val isValidEmailAddress =
                    android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
                if (!isValidEmailAddress) {
                    emailAddressEditText.error = "Enter a valid email address."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getSignUpButtonAccess(
                    emailAddressEditText,
                    passwordEditText,
                    logInButton
                )
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password: String = passwordEditText.text.toString().trim()
                if (password.isNullOrEmpty()) {
                    passwordEditText.error =
                        "Enter password"
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getSignUpButtonAccess(
                    emailAddressEditText,
                    passwordEditText,
                    logInButton
                )
            }
        })

        forgotPasswordTextView.setOnClickListener {
            val intent =
                Intent(this@SignInActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        logInButton.setOnClickListener {
            val email = emailAddressEditText.text.toString()
            val password = passwordEditText.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.signInWith(Email) {
                        this.email = email
                        this.password = password
                    }

                    supabase.auth.sessionStatus.collect {
                        when (it) {
                            is SessionStatus.Authenticated -> {
                                if (rememberPasswordChkBx.isChecked) {
                                    val session = supabase.auth.currentSessionOrNull()
                                    if (session != null) {
                                        supabase.auth.sessionManager.saveSession(session)
                                    }
                                }
                                val intent =
                                    Intent(this@SignInActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            SessionStatus.NetworkError -> Toast.makeText(
                                this@SignInActivity,
                                "Network error",
                                Toast.LENGTH_LONG
                            ).show()

                            SessionStatus.NotAuthenticated -> Toast.makeText(
                                this@SignInActivity,
                                "Wrong email or password",
                                Toast.LENGTH_LONG
                            ).show()

                            else -> Toast.makeText(
                                this@SignInActivity,
                                "Whoops! Something went wrong",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (error: Exception) {
                    Log.d("logInException", error.toString())
                    var snackbar =
                        Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }

        signInGoogleImage.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    signInGoogle()
                    supabase.auth.sessionStatus.collect {
                        when (it) {
                            is SessionStatus.Authenticated -> {
                                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            SessionStatus.NetworkError -> getError(rootView, "Network Error")

                            SessionStatus.NotAuthenticated -> getError(
                                rootView,
                                "Authorization failed"
                            )

                            SessionStatus.LoadingFromStorage -> getError(
                                rootView,
                                "Loading session"
                            )

                            else -> getError(rootView, "Whoops! Something went wrong")
                        }
                    }
                } catch (error: Exception) {
                    Log.d("logInException", error.toString())
                    val snackbar = Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }

        signUnTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://cvrihxuwwzuqfxulrjne.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN2cmloeHV3d3p1cWZ4dWxyam5lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkxNDM2MDEsImV4cCI6MjAyNDcxOTYwMX0.tBvqGRNPAmzW3BLfI-6KRUxPREzEsbgatzROVBcLDJY"
    ) {
        install(Auth)
    }

    suspend fun signInGoogle() {
        withContext(Dispatchers.IO) {
            val user = supabase.auth.signInWith(Google)
        }
    }

    private fun getSignUpButtonAccess(
        emailAddressEditText: TextView,
        passwordEditText: TextView,
        signInButton: Button,
    ) {
        val emailCheck =
            !emailAddressEditText.text.isNullOrEmpty() && emailAddressEditText.error.isNullOrEmpty()
        val passwordCheck =
            !passwordEditText.text.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()

        if (emailCheck && passwordCheck) {
            signInButton.isClickable = true
            val color = Color.parseColor("#0560FA")
            signInButton.setBackgroundColor(color)
        } else {
            signInButton.isClickable = false
            val color = Color.parseColor("#A7A7A7")
            signInButton.setBackgroundColor(color)
        }
    }

    private fun getError(view: View, error: String) {
        Snackbar.make(view, error, Snackbar.LENGTH_LONG).show()
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}
