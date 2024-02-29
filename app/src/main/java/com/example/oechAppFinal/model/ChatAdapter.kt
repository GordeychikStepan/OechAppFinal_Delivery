package com.example.oechAppFinal.model

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oechAppFinal.R
import com.example.oechAppFinal.activity.ChatsMessagesActivity

class ChatAdapter(private val chats: List<Chat>, private val dark: Boolean) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutRes = if (dark) R.layout.item_chat_dark else R.layout.item_chat
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        holder.bind(chat)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private val unreadCountTextView: TextView = itemView.findViewById(R.id.unreadCountTextView)

        fun bind(chat: Chat) {
            // устанавливаем данные для каждого элемента чата
            profileImageView.setImageResource(chat.profileImage)
            nameTextView.text = chat.name
            messageTextView.text = chat.lastMessage
            if (chat.unreadCount > 0) {
                unreadCountTextView.visibility = View.VISIBLE
                unreadCountTextView.text = chat.unreadCount.toString()
            } else {
                unreadCountTextView.visibility = View.GONE
            }

            // обработка нажатия на элемент чата
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ChatsMessagesActivity::class.java)
                intent.putExtra("chatName", chat.name)
                intent.putExtra("chatImage", chat.profileImage.toString())
                itemView.context.startActivity(intent)
            }
        }
    }
}