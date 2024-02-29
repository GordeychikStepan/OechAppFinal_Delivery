package com.example.oechAppFinal.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.Constances
import com.example.oechAppFinal.model.User
import com.google.android.material.snackbar.Snackbar
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setTheme(R.style.Theme_OechAppFinal)

        val textView8 = findViewById<TextView>(R.id.textView8)
        val signInTextView = findViewById<TextView>(R.id.signInTextView)
        val fullNameEditText = findViewById<TextView>(R.id.fullNameEditText)
        val phoneNumberEditText = findViewById<TextView>(R.id.phoneNumberEditText)
        val emailAddressEditText = findViewById<TextView>(R.id.emailAddressEditText)
        val passwordEditText = findViewById<TextView>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<TextView>(R.id.confirmPasswordEditText)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val passwordVisibilityToggle1 = findViewById<ImageView>(R.id.passwordVisibilityToggle1)
        val passwordVisibilityToggle2 = findViewById<ImageView>(R.id.passwordVisibilityToggle2)
        val signInGoogleImage = findViewById<ImageView>(R.id.signInGoogleImage)
        val agrmntCheckBox = findViewById<CheckBox>(R.id.agreementCheckBox)
        val rootView = findViewById<View>(android.R.id.content)

        // изменение цвета политики
        val text = getString(R.string.private_policy)
        val spannableString = SpannableString(text)
        val startIndex = text.indexOf("our")
        if (startIndex != -1) {

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    val file = File(cacheDir, "privacy_policy_oech_app_final.pdf")
                    val fileOutputStream = FileOutputStream(file)
                    val inputStream = assets.open("privacy_policy_oech_app_final.pdf")

                    try {
                        inputStream.use { input ->
                            fileOutputStream.use { output ->
                                input.copyTo(output)
                            }
                        }
                    } finally {
                        fileOutputStream.close()
                        inputStream.close()
                    }

                    val fileUri = FileProvider.getUriForFile(
                        this@SignUpActivity,
                        applicationContext.packageName + ".provider",
                        file
                    )
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(fileUri, "application/pdf")
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    val highlightColor = ContextCompat.getColor(this@SignUpActivity, R.color.yellow_warning_color)
                    ds.color = highlightColor
                    ds.isUnderlineText = false
                }
            }

            spannableString.setSpan(
                clickableSpan,
                startIndex,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        textView8.movementMethod = LinkMovementMethod.getInstance()
        textView8.text = spannableString


        // обработчики нажатия на кнопку глаза
        passwordVisibilityToggle1.setOnClickListener {
            if (passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        passwordVisibilityToggle2.setOnClickListener {
            if (confirmPasswordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                confirmPasswordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                confirmPasswordEditText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

        fullNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val fullName: String = fullNameEditText.text.toString().trim()
                val fullNamePattern = Regex("[А-Яа-яA-Za-z]+\\s[А-Яа-яA-Za-z]+")
                val isValidFullName = fullName.matches(fullNamePattern)
                if (!isValidFullName) {
                    fullNameEditText.error =
                        "Please enter your full name in the format 'Last Name First Name'."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getSignUpButtonAccess(
                    fullNameEditText,
                    phoneNumberEditText,
                    emailAddressEditText,
                    passwordEditText,
                    confirmPasswordEditText,
                    signUpButton,
                    agrmntCheckBox
                )
            }
        })

        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val phoneNumber: String = phoneNumberEditText.text.toString().trim()
                val phoneNumberPattern = Regex("^\\+7\\d{10}$")
                val isValidPhoneNumber = phoneNumber.matches(phoneNumberPattern)
                if (!isValidPhoneNumber) {
                    phoneNumberEditText.error = "Please enter the correct phone number."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getSignUpButtonAccess(
                    fullNameEditText,
                    phoneNumberEditText,
                    emailAddressEditText,
                    passwordEditText,
                    confirmPasswordEditText,
                    signUpButton,
                    agrmntCheckBox
                )
            }
        })

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
                    fullNameEditText,
                    phoneNumberEditText,
                    emailAddressEditText,
                    passwordEditText,
                    confirmPasswordEditText,
                    signUpButton,
                    agrmntCheckBox
                )
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

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
                getSignUpButtonAccess(
                    fullNameEditText,
                    phoneNumberEditText,
                    emailAddressEditText,
                    passwordEditText,
                    confirmPasswordEditText,
                    signUpButton,
                    agrmntCheckBox
                )
            }
        })

        confirmPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password: String = passwordEditText.text.toString().trim()
                val confirmPassword: String = confirmPasswordEditText.text.toString().trim()
                if (password != confirmPassword) {
                    confirmPasswordEditText.error = "Password doesn't match."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                getSignUpButtonAccess(
                    fullNameEditText,
                    phoneNumberEditText,
                    emailAddressEditText,
                    passwordEditText,
                    confirmPasswordEditText,
                    signUpButton,
                    agrmntCheckBox
                )
            }
        })

        agrmntCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            getSignUpButtonAccess(
                fullNameEditText,
                phoneNumberEditText,
                emailAddressEditText,
                passwordEditText,
                confirmPasswordEditText,
                signUpButton,
                agrmntCheckBox
            )
        }

        signInTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        signUpButton.setOnClickListener {
            val email = emailAddressEditText.text.toString()
            val password = passwordEditText.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.signUpWith(Email) {
                        this.email = email
                        this.password = password
                    }
                    if (user.toString().isNotEmpty()){
                        getError(rootView, "Registration successfully ")
                        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (error: Exception) {
                    Log.d("logInException", error.toString())
                    val snackbar = Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }

        signInGoogleImage.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.signUpWith(Google)
                    if (user.toString().isNotEmpty()){
                        getError(rootView, "Registration successfully ")
                        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (error: Exception) {
                    Log.d("logInException", error.toString())
                    val snackbar =
                        Snackbar.make(it, R.string.something_wrong, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }
    }

    private fun getSignUpButtonAccess(
        fullNameEditText: TextView,
        phoneNumberEditText: TextView,
        emailAddressEditText: TextView,
        passwordEditText: TextView,
        confirmPasswordEditText: TextView,
        signUpButton: Button,
        agrmntCheckBox: CheckBox
    ) {
        val fullNameCheck = !fullNameEditText.text.isNullOrEmpty() && fullNameEditText.error.isNullOrEmpty()
        val phoneNumberCheck = !phoneNumberEditText.text.isNullOrEmpty() && phoneNumberEditText.error.isNullOrEmpty()
        val emailCheck = !emailAddressEditText.text.isNullOrEmpty() && emailAddressEditText.error.isNullOrEmpty()
        val passwordCheck = !passwordEditText.text.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()
        val confirmPasswordCheck = !confirmPasswordEditText.text.isNullOrEmpty() && confirmPasswordEditText.error.isNullOrEmpty()

        if (fullNameCheck && phoneNumberCheck && emailCheck && passwordCheck && confirmPasswordCheck && agrmntCheckBox.isChecked) {
            signUpButton.isClickable = true
            val color = Color.parseColor("#0560FA")
            signUpButton.setBackgroundColor(color)
        } else {
            signUpButton.isClickable = false
            val color = Color.parseColor("#A7A7A7")
            signUpButton.setBackgroundColor(color)
        }
    }

    val supabase = createSupabaseClient(
        supabaseUrl = "https://cvrihxuwwzuqfxulrjne.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN2cmloeHV3d3p1cWZ4dWxyam5lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkxNDM2MDEsImV4cCI6MjAyNDcxOTYwMX0.tBvqGRNPAmzW3BLfI-6KRUxPREzEsbgatzROVBcLDJY"
    ) {
        //install(Postgrest)
        install(Auth)
    }

    fun registredUser(view: View, error: String) {
        val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
        snackbar.setAction("Want to login?") {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        snackbar.setActionTextColor(ContextCompat.getColor(view.context, R.color.blue_primary))
        snackbar.show()
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