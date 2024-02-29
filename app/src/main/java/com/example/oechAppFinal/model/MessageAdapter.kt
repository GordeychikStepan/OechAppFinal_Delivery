package com.example.oechAppFinal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oechAppFinal.R

class MessageAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    var dark: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = if (viewType == VIEW_TYPE_ME) {
            if (dark) {
                layoutInflater.inflate(R.layout.item_message_me_dark, parent, false)
            } else {
                layoutInflater.inflate(R.layout.item_message_me, parent, false)
            }
        } else {
            if (dark) {
                layoutInflater.inflate(R.layout.item_message_other_dark, parent, false)
            } else {
                layoutInflater.inflate(R.layout.item_message_other, parent, false)
            }
        }
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        // определяем тип сообщения (от вас или от собеседника)
        return if (messages[position].isFromMe) {
            VIEW_TYPE_ME
        } else {
            VIEW_TYPE_OTHER
        }
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

        fun bind(message: Message) {
            messageTextView.text = message.text
        }
    }

    companion object {
        private const val VIEW_TYPE_ME = 1
        private const val VIEW_TYPE_OTHER = 2
    }
}