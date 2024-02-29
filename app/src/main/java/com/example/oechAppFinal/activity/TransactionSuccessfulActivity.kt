package com.example.oechAppFinal.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.oechAppFinal.R
import com.example.oechAppFinal.databinding.ActivityTransactionSuccessfulBinding
import com.example.oechAppFinal.model.Constances

class TransactionSuccessfulActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionSuccessfulBinding
    private var signState = ""
    private lateinit var loadingIcon: ImageView
    private var animationCount = 0
    private val maxAnimationCount = 5 // количество повторений второй анимации
    private lateinit var transactionSuccessfulText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionSuccessfulBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (getSavedThemeState())
        {
            binding.layout.setBackgroundColor(resources.getColor(R.color.blue_primary_shade1, theme))
            binding.transactionSuccessfulText.setTextColor(resources.getColor(R.color.white, theme))
            binding.rider.setTextColor(resources.getColor(R.color.white, theme))
            binding.trackingNumberText.setTextColor(resources.getColor(R.color.white, theme))
            binding.feedbackEditText.setTextColor(resources.getColor(R.color.white))
            binding.feedbackEditText.setHintTextColor(resources.getColor(R.color.light_gray_color))
        }

        signState = intent.getStringExtra(Constances.CODE)!!

        when (signState) {
            Constances.ONE -> {
                binding.llButtons.visibility = VISIBLE
                binding.Text.visibility = VISIBLE
            }

            else -> {
                binding.llReports.visibility = VISIBLE
                binding.Text.visibility = VISIBLE
            }
        }

        val trackingNumberTextView: TextView = binding.trackingNumberTextView
        val trackItemButton: Button = binding.trackItemButton
        val goBackButton: Button = binding.goBackButton
        loadingIcon = binding.loadingIcon
        transactionSuccessfulText = binding.transactionSuccessfulText

        transactionSuccessfulText.visibility = View.INVISIBLE

        // трек-номер транзакции
        val transactionTrackingNumber = "R-7458-4567-4434-5854"
        val trackingNumberText = "Tracking Number:"

        val color = ContextCompat.getColor(this, R.color.blue_primary)

        // запуск анимации загрузки
        startLoadingAnimation()

        // после задержки, запускаем анимацию смены галочек
        // (старт анимации можно сделать после подтверждения транзакции)
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            transactionSuccessfulText.visibility = VISIBLE
            loadingIcon.clearAnimation()
            startCheckAnimation()
        }, 5000)


        val star1: ImageView = binding.star1
        val star2: ImageView = binding.star2
        val star3: ImageView = binding.star3
        val star4: ImageView = binding.star4
        val star5: ImageView = binding.star5

        star1.setOnClickListener {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star)
        }

        star2.setOnClickListener {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star)
        }

        star3.setOnClickListener {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)
            star4.setImageResource(R.drawable.ic_star)
            star5.setImageResource(R.drawable.ic_star)
        }

        star4.setOnClickListener {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)
            star4.setImageResource(R.drawable.ic_star_filled)
            star5.setImageResource(R.drawable.ic_star)
        }

        star5.setOnClickListener {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)
            star4.setImageResource(R.drawable.ic_star_filled)
            star5.setImageResource(R.drawable.ic_star_filled)
        }

        binding.trackItemButton.setOnClickListener {
            val intent = Intent()
            val pref = applicationContext.getSharedPreferences("RedirectData", 0)
            pref.edit().putInt("nav_id", 2131362213).apply()
            intent.putExtra(Constances.CODE, Constances.TRACK)
            setResult(RESULT_FIRST_USER, intent)
            finish()
        }

        binding.goBackButton.setOnClickListener {
            val intent = Intent()
            val pref = applicationContext.getSharedPreferences("RedirectData", 0)
            pref.edit().putInt("nav_id", 2131362211).apply()
            intent.putExtra(Constances.CODE, Constances.HOME)
            setResult(RESULT_FIRST_USER, intent)
            finish()
        }

        binding.doneButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Constances.CODE, Constances.HOME)
            setResult(RESULT_FIRST_USER, intent)
            finish()
        }
    }

    private fun startLoadingAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.spin_animation)
        loadingIcon.startAnimation(animation)
    }

    private fun startCheckAnimation() {
        val handler = android.os.Handler(Looper.getMainLooper())
        val delay: Long = 300
        val images = arrayOf(R.drawable.check_image1, R.drawable.check_image2)

        val runnable = object : Runnable {
            override fun run() {
                if (animationCount < maxAnimationCount) {
                    loadingIcon.setImageResource(images[animationCount % images.size])
                    animationCount++
                    handler.postDelayed(this, delay)
                } else {
                    loadingIcon.clearAnimation()
                    loadingIcon.setImageResource(R.drawable.check_image1) // оставляем галочку
                }
            }
        }

        loadingIcon.setImageResource(images[0])
        handler.postDelayed(runnable, delay)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}