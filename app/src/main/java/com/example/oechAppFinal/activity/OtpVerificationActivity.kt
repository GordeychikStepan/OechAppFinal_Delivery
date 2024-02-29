package com.example.oechAppFinal.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.Constances
import com.google.android.material.snackbar.Snackbar
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class OtpVerificationActivity : AppCompatActivity() {

    private var validCode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_vrification)

        setTheme(R.style.Theme_OechAppFinal)

        val textView3 = findViewById<TextView>(R.id.textView3)
        val editText1 = findViewById<EditText>(R.id.editText1)
        val editText2 = findViewById<EditText>(R.id.editText2)
        val editText3 = findViewById<EditText>(R.id.editText3)
        val editText4 = findViewById<EditText>(R.id.editText4)
        val editText5 = findViewById<EditText>(R.id.editText5)
        val editText6 = findViewById<EditText>(R.id.editText6)
        val resendTextView = findViewById<TextView>(R.id.resendTextView)
        val sendOtpButton = findViewById<Button>(R.id.sendOtpButton)
        val rootView: View = findViewById<View>(android.R.id.content)

        val bluePrimaryColor = ContextCompat.getColor(this, R.color.blue_primary)
        val grayColor = ContextCompat.getColor(this, R.color.gray_color)

        // таймер на 1 минуту
        val initialTime = 1 * 60 * 1000
        var currentTime = initialTime
        val timer = object : CountDownTimer(currentTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resendTextView.setTextColor(grayColor)
                currentTime = millisUntilFinished.toInt()
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                resendTextView.text = String.format("resend after %d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                resendTextView.text = "resend"
                resendTextView.setTextColor(bluePrimaryColor)

                val widthInPixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    416.toFloat(),
                    resources.displayMetrics
                ).toInt()

                val params = textView3.layoutParams
                params.width = widthInPixels
                textView3.layoutParams = params

                resendTextView.isClickable = true
            }
        }
        timer.start()

        resendTextView.setOnClickListener {
            // код для отправки запроса (еще раз)
            val email = intent.getStringExtra("email")
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.resetPasswordForEmail(email!!)
                } catch (e: Exception) {
                    Log.d("otpException", e.toString())
                    Snackbar.make(rootView, "Wrong code", Snackbar.LENGTH_SHORT).show()
                }
            }

            val widthInPixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                354.toFloat(),
                resources.displayMetrics
            ).toInt()
            val params = textView3.layoutParams
            params.width = widthInPixels
            textView3.layoutParams = params
            resendTextView.isClickable = false
            timer.start()
        }

        sendOtpButton.setOnClickListener {
            val code = editText1.text.toString().trim() + editText2.text.toString().trim() + editText3.text.toString().trim() + editText4.text.toString().trim() + editText5.text.toString().trim() + editText6.text.toString().trim()
            val email = intent.getStringExtra("email")
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val user = supabase.auth.verifyEmailOtp(OtpType.Email.RECOVERY, email!!, code)
                    supabase.auth.sessionStatus.collect {
                        when (it) {
                            is SessionStatus.Authenticated -> {
                                validCode = true
                                val intent = Intent(this@OtpVerificationActivity, NewPasswordActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            SessionStatus.NetworkError -> {}
                            SessionStatus.NotAuthenticated -> {}
                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    Log.d("otpException", e.toString())
                    editText1.text.clear()
                    editText2.text.clear()
                    editText3.text.clear()
                    editText4.text.clear()
                    editText5.text.clear()
                    editText6.text.clear()
                    Snackbar.make(rootView, "Wrong code", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        editText1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editText1.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    editText1.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                    editText2.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editText2.clearFocus()
                    editText1.requestFocus()
                    editText2.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    editText3.requestFocus()
                    editText2.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editText3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editText3.clearFocus()
                    editText2.requestFocus()
                    editText3.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    editText4.requestFocus()
                    editText3.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editText4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editText4.clearFocus()
                    editText3.requestFocus()
                    editText4.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    editText5.requestFocus()
                    editText4.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editText5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editText5.clearFocus()
                    editText4.requestFocus()
                    editText5.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    editText6.requestFocus()
                    editText5.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editText6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editText6.clearFocus()
                    editText5.requestFocus()
                    editText6.setBackgroundResource(R.drawable.otp_verification_edittext)
                    return
                }
                if (s.length == 1) {
                    editText6.setBackgroundResource(R.drawable.otp_verification_edittext_filled)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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