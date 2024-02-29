package com.example.oechAppFinal.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.oechAppFinal.R
import com.example.oechAppFinal.activity.ChatsActivity
import com.example.oechAppFinal.databinding.FragmentHomeBinding
import com.example.oechAppFinal.model.Constances

class HomeFragment : Fragment() {

    private lateinit var home: FragmentHomeBinding

    private lateinit var choiceLinearLayout1: LinearLayout
    private lateinit var choiceLinearLayout2: LinearLayout
    private lateinit var choiceLinearLayout3: LinearLayout
    private lateinit var choiceLinearLayout4: LinearLayout

    private lateinit var cardImageView1: ImageView
    private lateinit var cardImageView2: ImageView
    private lateinit var cardImageView3: ImageView
    private lateinit var cardImageView4: ImageView

    private lateinit var cardTextView1_1: TextView
    private lateinit var cardTextView1_2: TextView
    private lateinit var cardTextView2_1: TextView
    private lateinit var cardTextView2_2: TextView
    private lateinit var cardTextView3_1: TextView
    private lateinit var cardTextView3_2: TextView
    private lateinit var cardTextView4_1: TextView
    private lateinit var cardTextView4_2: TextView

    private lateinit var searchServicesEditText: EditText

    private lateinit var profileInfoBackgroundLinearLayout: LinearLayout

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        home = FragmentHomeBinding.inflate(inflater, container, false)
        return home.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choiceLinearLayout1 = home.root.findViewById(R.id.choiceLinearLayout1)
        choiceLinearLayout2 = home.root.findViewById(R.id.choiceLinearLayout2)
        choiceLinearLayout3 = home.root.findViewById(R.id.choiceLinearLayout3)
        choiceLinearLayout4 = home.root.findViewById(R.id.choiceLinearLayout4)

        cardImageView1 = home.root.findViewById(R.id.cardImageView1)
        cardImageView2 = home.root.findViewById(R.id.cardImageView2)
        cardImageView3 = home.root.findViewById(R.id.cardImageView3)
        cardImageView4 = home.root.findViewById(R.id.cardImageView4)

        cardTextView1_1 = home.root.findViewById(R.id.cardTextView1_1)
        cardTextView1_2 = home.root.findViewById(R.id.cardTextView1_2)
        cardTextView2_1 = home.root.findViewById(R.id.cardTextView2_1)
        cardTextView2_2 = home.root.findViewById(R.id.cardTextView2_2)
        cardTextView3_1 = home.root.findViewById(R.id.cardTextView3_1)
        cardTextView3_2 = home.root.findViewById(R.id.cardTextView3_2)
        cardTextView4_1 = home.root.findViewById(R.id.cardTextView4_1)
        cardTextView4_2 = home.root.findViewById(R.id.cardTextView4_2)

        searchServicesEditText = home.root.findViewById(R.id.searchServicesEditText)

        profileInfoBackgroundLinearLayout = home.root.findViewById(R.id.profileInfoBackgroundLinearLayout)

        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        val colorPrimary = ContextCompat.getColor(requireContext(), R.color.blue_primary)
        val colorPrimaryShade1 = ContextCompat.getColor(requireContext(), R.color.blue_primary_shade1)
        val colorLightBlack = ContextCompat.getColor(requireContext(), R.color.light_black_text_color)

        val isDark: Boolean = getSavedThemeState()

        if (isDark)
        {
            home.root.setBackgroundColor(colorPrimaryShade1)
            choiceLinearLayout1.setBackgroundResource(R.drawable.choice_card_dark)
            choiceLinearLayout2.setBackgroundResource(R.drawable.choice_card_dark)
            choiceLinearLayout3.setBackgroundResource(R.drawable.choice_card_dark)
            choiceLinearLayout4.setBackgroundResource(R.drawable.choice_card_dark)
            cardTextView1_2.setTextColor(colorWhite)
            cardTextView2_2.setTextColor(colorWhite)
            cardTextView3_2.setTextColor(colorWhite)
            cardTextView4_2.setTextColor(colorWhite)
            searchServicesEditText.setBackgroundResource(R.drawable.default_edittext_to_search_dark)
            profileInfoBackgroundLinearLayout.setBackgroundResource(R.drawable.profile_info_background_dark)
        }

        choiceLinearLayout1.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val fadeIn = AlphaAnimation(0f, 1f)
                    fadeIn.duration = 150
                    choiceLinearLayout1.startAnimation(fadeIn)

                    choiceLinearLayout1.setBackgroundResource(R.drawable.choice_card_blue)
                    cardTextView1_1.setTextColor(colorWhite)
                    cardTextView1_2.setTextColor(colorWhite)
                    cardImageView1.setImageResource(R.drawable.ic_customer_care_white)
                }
                MotionEvent.ACTION_UP -> {
                    val fadeIn = AlphaAnimation(0f, 1f)
                    fadeIn.duration = 50
                    choiceLinearLayout1.startAnimation(fadeIn)

                    if (isDark) {
                        choiceLinearLayout1.setBackgroundResource(R.drawable.choice_card_dark)
                        cardTextView1_1.setTextColor(colorPrimary)
                        cardTextView1_2.setTextColor(colorWhite)
                        cardImageView1.setImageResource(R.drawable.ic_customer_care_blue)
                    } else {
                        choiceLinearLayout1.setBackgroundResource(R.drawable.choice_card_white)
                        cardTextView1_1.setTextColor(colorPrimary)
                        cardTextView1_2.setTextColor(colorLightBlack)
                        cardImageView1.setImageResource(R.drawable.ic_customer_care_blue)
                    }
                }
            }
            true
        }

        choiceLinearLayout2.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val fadeIn = AlphaAnimation(0f, 1f)
                    fadeIn.duration = 150
                    choiceLinearLayout2.startAnimation(fadeIn)

                    choiceLinearLayout2.setBackgroundResource(R.drawable.choice_card_blue)
                    cardTextView2_1.setTextColor(colorWhite)
                    cardTextView2_2.setTextColor(colorWhite)
                    cardImageView2.setImageResource(R.drawable.ic_send_a_package_white)
                }
                MotionEvent.ACTION_UP -> {
                    val fadeIn = AlphaAnimation(0f, 1f)
                    fadeIn.duration = 50
                    choiceLinearLayout2.startAnimation(fadeIn)

                    if (isDark) {
                        choiceLinearLayout2.setBackgroundResource(R.drawable.choice_card_dark)
                        cardTextView2_1.setTextColor(colorPrimary)
                        cardTextView2_2.setTextColor(colorWhite)
                        cardImageView2.setImageResource(R.drawable.ic_send_a_package_blue)
                    } else {
                        choiceLinearLayout2.setBackgroundResource(R.drawable.choice_card_white)
                        cardTextView2_1.setTextColor(colorPrimary)
                        cardTextView2_2.setTextColor(colorLightBlack)
                        cardImageView2.setImageResource(R.drawable.ic_send_a_package_blue)
                    }
                }
            }
            true
        }

        choiceLinearLayout3.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val fadeIn = AlphaAnimation(0f, 1f)
                    fadeIn.duration = 150
                    choiceLinearLayout3.startAnimation(fadeIn)

                    choiceLinearLayout3.setBackgroundResource(R.drawable.choice_card_blue)
                    cardTextView3_1.setTextColor(colorWhite)
                    cardTextView3_2.setTextColor(colorWhite)
                    cardImageView3.setImageResource(R.drawable.ic_fund_a_wallet_white)
                }
                MotionEvent.ACTION_UP -> {
                    val fadeIn = AlphaAnimation(0f, 1f)
                    fadeIn.duration = 50
                    choiceLinearLayout3.startAnimation(fadeIn)

                    if (isDark) {
                        choiceLinearLayout3.setBackgroundResource(R.drawable.choice_card_dark)
                        cardTextView3_1.setTextColor(colorPrimary)
                        cardTextView3_2.setTextColor(colorWhite)
                        cardImageView3.setImageResource(R.drawable.ic_fund_a_wallet_blue)
                    } else {
                        choiceLinearLayout3.setBackgroundResource(R.drawable.choice_card_white)
                        cardTextView3_1.setTextColor(colorPrimary)
                        cardTextView3_2.setTextColor(colorLightBlack)
                        cardImageView3.setImageResource(R.drawable.ic_fund_a_wallet_blue)
                    }
                }
            }
            true
        }

        choiceLinearLayout4.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val fadeIn = AlphaAnimation(0f, 1f)
                    fadeIn.duration = 150
                    choiceLinearLayout4.startAnimation(fadeIn)

                    choiceLinearLayout4.setBackgroundResource(R.drawable.choice_card_blue)
                    cardTextView4_1.setTextColor(colorWhite)
                    cardTextView4_2.setTextColor(colorWhite)
                    cardImageView4.setImageResource(R.drawable.ic_chats_white)
                }
                MotionEvent.ACTION_UP -> {
                    val fadeIn = AlphaAnimation(0f, 1f)
                    fadeIn.duration = 50
                    choiceLinearLayout4.startAnimation(fadeIn)

                    if (isDark) {
                        choiceLinearLayout4.setBackgroundResource(R.drawable.choice_card_dark)
                        cardTextView4_1.setTextColor(colorPrimary)
                        cardTextView4_2.setTextColor(colorWhite)
                        cardImageView4.setImageResource(R.drawable.ic_chats_blue)
                    } else {
                        choiceLinearLayout4.setBackgroundResource(R.drawable.choice_card_white)
                        cardTextView4_1.setTextColor(colorPrimary)
                        cardTextView4_2.setTextColor(colorLightBlack)
                        cardImageView4.setImageResource(R.drawable.ic_chats_blue)
                    }

                    startActivity(
                        Intent(
                            this.requireContext(),
                            ChatsActivity::class.java
                        )
                    )

                }
            }

            true
        }
    }
}