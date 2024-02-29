package com.example.oechAppFinal.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oechAppFinal.R
import com.example.oechAppFinal.databinding.ActivityChatsBinding
import com.example.oechAppFinal.model.Chat
import com.example.oechAppFinal.model.ChatAdapter
import com.example.oechAppFinal.model.ChatDriver
import com.example.oechAppFinal.model.ChatDriverAdapter
import com.example.oechAppFinal.model.Constances

class ChatsActivity : AppCompatActivity() {

    private lateinit var chatsRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatDriverAdapter: ChatDriverAdapter

    private var chats: List<Chat> = emptyList()
    private var chatsDriver: List<ChatDriver> = emptyList()

    private lateinit var binding: ActivityChatsBinding
    private var isCustomer: Boolean = Constances.isCustomer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chats"

        val searchChatsEditText: EditText = findViewById(R.id.enterMessageEditText)
        val searchImageView: ImageView = findViewById(R.id.searchImageView)

        if (getSavedThemeState()) {
            binding.layout.setBackgroundColor(resources.getColor(R.color.blue_primary_shade1, theme))
            searchChatsEditText.setBackgroundResource(R.drawable.default_edittext_to_search_dark)
            searchChatsEditText.setTextColor(ContextCompat.getColor(this, R.color.white))
            searchChatsEditText.setHintTextColor(ContextCompat.getColor(this, R.color.white))
            searchImageView.setImageResource(R.drawable.search_normal_dark)
        } else {
            binding.layout.setBackgroundColor(resources.getColor(R.color.white, theme))
        }

        chatsRecyclerView = findViewById(R.id.chatsRecyclerView)

        val layoutManager = LinearLayoutManager(this)
        chatsRecyclerView.layoutManager = layoutManager

        if (isCustomer) {
            chatsDriver = listOf(
                ChatDriver(R.drawable.profile_avatar, "John Joshua", "R-456-223U", 4, "Toyota Camry", "M"),
                ChatDriver(R.drawable.profile_avatar_driver_1, "Saviour Bill", "R-432-723K", 3, "Ford Fusion", "M"),
                ChatDriver(R.drawable.profile_avatar_2, "Chinonso James", "R-359-224G", 5, "Honda Accord", "M"),
                ChatDriver(R.drawable.profile_avatar_3, "Raph Ron", "R-890-245N", 4, "Nissan Altima", "M"),
                ChatDriver(R.drawable.profile_avatar_4, "Joy Ezekiel", "R-434-221C", 4, "Chevrolet Malibu", "W"),
                ChatDriver(R.drawable.profile_avatar_5, "Wonder Obi", "R-345-267V", 3, "Hyundai Sonata", "W"),
                ChatDriver(R.drawable.profile_avatar_driver_2, "Amara Chidi", "R-673-651B", 2, "Kia Optima", "W"),
                ChatDriver(R.drawable.profile_avatar_driver_3, "Suleman Adebayo", "R-556-226U", 5, "Volkswagen Passat", "M"),
                ChatDriver(R.drawable.profile_avatar_driver_4, "Musa Francis", "R-486-267X", 5, "Subaru Legacy", "M"),
                ChatDriver(R.drawable.profile_avatar_driver_5, "John Kalu", "R-136-323U", 3, "BMW 3 Series", "M"),
                ChatDriver(R.drawable.profile_avatar_driver_6, "Hustle Joshua", "R-904-213J", 2, "Mercedes-Benz C-Class", "W"),
                ChatDriver(R.drawable.profile_avatar_driver_7, "Best Obasanjo", "R-676-221Z", 5, "Audi A4", "W"),
                ChatDriver(R.drawable.profile_avatar_driver_8, "Lionel Messi", "R-256-903U", 5, "Lexus ES", "M"),
                ChatDriver(R.drawable.profile_avatar_driver_9, "Lionel Messi", "R-256-903U", 5, "Acura TLX", "M")
            )
            chatDriverAdapter = if (getSavedThemeState()) {
                ChatDriverAdapter(chatsDriver, true)
            } else {
                ChatDriverAdapter(chatsDriver, false)
            }
            chatDriverAdapter.setOnItemClickListener(object : ChatDriverAdapter.OnItemClickListener {
                override fun onItemClick(chatDriver: ChatDriver) {
                    val intent = Intent(this@ChatsActivity, DriverProfileActivity::class.java)
                    intent.putExtra("chatDriver", chatDriver)
                    startActivity(intent)
                }
            })
            chatAdapter = if (getSavedThemeState()) {
                ChatAdapter(chats, true)
            } else {
                ChatAdapter(chats, false)
            }
            chatsRecyclerView.adapter = chatDriverAdapter
        } else {
            chats = listOf(
                Chat(R.drawable.profile_avatar, "John Joshua", "Thanks for your service", 1),
                Chat(R.drawable.profile_avatar_2, "Chinonso James", "Alright, I wll be waiting", 0),
                Chat(R.drawable.profile_avatar_3, "Raph Ron", "Thanks for your service", 5),
                Chat(R.drawable.profile_avatar_4, "Joy Ezekiel", "Thanks for your service", 0),
                Chat(R.drawable.profile_avatar_5, "Joy Ezekiel", "Thanks for your service", 1)
            )
            chatAdapter = if (getSavedThemeState()) {
                ChatAdapter(chats, true)
            } else {
                ChatAdapter(chats, false)
            }
            chatsRecyclerView.adapter = chatAdapter
        }

        searchChatsEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isCustomer) {
                    val filteredChats = filterDriverChats(s.toString())
                    chatDriverAdapter = if (getSavedThemeState()) {
                        ChatDriverAdapter(filteredChats, true)
                    } else {
                        ChatDriverAdapter(filteredChats, false)
                    }
                    chatDriverAdapter.setOnItemClickListener(object : ChatDriverAdapter.OnItemClickListener {
                        override fun onItemClick(chatDriver: ChatDriver) {
                            val intent = Intent(this@ChatsActivity, DriverProfileActivity::class.java)
                            intent.putExtra("chatDriver", chatDriver)
                            startActivity(intent)
                        }
                    })
                    chatsRecyclerView.adapter = chatDriverAdapter
                } else {
                    val filteredChats = filterChats(s.toString())
                    chatAdapter = if (getSavedThemeState()) {
                        ChatAdapter(filteredChats, true)
                    } else {
                        ChatAdapter(filteredChats, false)
                    }
                    chatsRecyclerView.adapter = chatAdapter
                }
            }
        })
    }

    private fun filterChats(query: String): List<Chat> {
        return chats.filter { chat ->
            chat.name.contains(query, ignoreCase = true)
        }
    }

    private fun filterDriverChats(query: String): List<ChatDriver> {
        return chatsDriver.filter { chatsDriver ->
            chatsDriver.name.contains(query, ignoreCase = true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
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