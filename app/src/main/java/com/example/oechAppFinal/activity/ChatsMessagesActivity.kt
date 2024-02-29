package com.example.oechAppFinal.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oechAppFinal.R
import com.example.oechAppFinal.databinding.ActivityChatsMessagesBinding
import com.example.oechAppFinal.model.Constances
import com.example.oechAppFinal.model.Message
import com.example.oechAppFinal.model.MessageAdapter

class ChatsMessagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatsMessagesBinding

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatName = intent.getStringExtra("chatName")
        val chatImageName = intent.getStringExtra("chatImage")

        val backIcon: ImageView = binding.backIcon
        val callIcon: ImageView = binding.callIcon
        val actionbarText: TextView = binding.actionbarText
        val chatImageView: ImageView = binding.chatImageView

        actionbarText.text = chatName

        if (chatImageName != null) {
            val resourceId = resources.getIdentifier(chatImageName, "drawable", packageName)
            if (resourceId != 0) {
                chatImageView.setImageResource(resourceId)
            } else {
                chatImageView.setImageResource(R.drawable.ic_profile)
            }
        } else {
            chatImageView.setImageResource(R.drawable.ic_profile)
        }

        backIcon.setOnClickListener {
            onBackPressed()
        }

        val messagesRecyclerView: RecyclerView = findViewById(R.id.messagesRecyclerView)
        val enterMessageEditText: EditText = findViewById(R.id.enterMessageEditText)
        val enterImageView: ImageView = findViewById(R.id.enterImageView)
        val onlineTextView: TextView = findViewById(R.id.onlineTextView)
        val emojiImageView: ImageView = findViewById(R.id.emojiImageView)
        val microImageView: ImageView = findViewById(R.id.microImageView)

        val layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.layoutManager = layoutManager

        val messages = mutableListOf(
            Message("Hello, Please check your phone, I just booked you to deliver my stuff", isFromMe = true),
            Message("Thank you for contacting me.", isFromMe = false),
            Message("I am already on my way to the pick up venue.", isFromMe = false),
            Message("Alright, I wll be waiting", isFromMe = true)
        )

        val adapter = MessageAdapter(messages)


        if (getSavedThemeState()) {
            binding.layout.setBackgroundColor(resources.getColor(R.color.blue_primary_shade1, theme))
            binding.actionbarLayout.setBackgroundColor(resources.getColor(R.color.blue_primary_shade1, theme))
            actionbarText.setTextColor(ContextCompat.getColor(this, R.color.white))
            enterMessageEditText.setBackgroundResource(R.drawable.default_edittext_to_search_dark)
            enterMessageEditText.setTextColor(ContextCompat.getColor(this, R.color.white))
            enterMessageEditText.setHintTextColor(ContextCompat.getColor(this, R.color.white))
            enterImageView.setImageResource(R.drawable.ic_triangle_dark)
            emojiImageView.setImageResource(R.drawable.ic_emoji_dark)
            microImageView.setImageResource(R.drawable.ic_dictation_dark)
            adapter.dark = true
        } else {
            binding.layout.setBackgroundColor(resources.getColor(R.color.white, theme))
            adapter.dark = false
        }

        messagesRecyclerView.adapter = adapter

        if (Constances.isCustomer) {
            onlineTextView.visibility = View.VISIBLE
        } else {
            onlineTextView.visibility = View.GONE
        }

        enterImageView.setOnClickListener {
            val messageText = enterMessageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Message(messageText, isFromMe = true)
                messages += newMessage
                adapter.notifyItemInserted(messages.size - 1)
                enterMessageEditText.text.clear()

                messagesRecyclerView.scrollToPosition(messages.size - 1)
            }
        }

        callIcon.setOnClickListener {
            val intent = Intent(this, CallRiderActivity::class.java)
            intent.putExtra("chatName", chatName)
            intent.putExtra("chatImage", chatImageName.toString())
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}