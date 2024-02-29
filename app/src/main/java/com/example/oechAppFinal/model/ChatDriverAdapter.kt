package com.example.oechAppFinal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oechAppFinal.R

class ChatDriverAdapter(private val chatDrivers: List<ChatDriver>, private val dark: Boolean) : RecyclerView.Adapter<ChatDriverAdapter.ChatDriverViewHolder>() {

    interface OnItemClickListener { fun onItemClick(chatDriver: ChatDriver) }
    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) { itemClickListener = listener }

    inner class ChatDriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val star1: ImageView = itemView.findViewById(R.id.driverStar1)
        val star2: ImageView = itemView.findViewById(R.id.driverStar2)
        val star3: ImageView = itemView.findViewById(R.id.driverStar3)
        val star4: ImageView = itemView.findViewById(R.id.driverStar4)
        val star5: ImageView = itemView.findViewById(R.id.driverStar5)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val chatDriver = chatDrivers[position]
                    itemClickListener?.onItemClick(chatDriver)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatDriverViewHolder {
        val layoutResId = if (dark) R.layout.item_chat_driver_dark else R.layout.item_chat_driver
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutResId, parent, false)
        return ChatDriverViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatDriverViewHolder, position: Int) {
        val chatDriver = chatDrivers[position]

        holder.profileImageView.setImageResource(chatDriver.profileImage)
        holder.nameTextView.text = chatDriver.name
        holder.messageTextView.text = chatDriver.registrationNumber

        val stars = chatDriver.stars
        val starViews = arrayOf(holder.star1, holder.star2, holder.star3, holder.star4, holder.star5)
        for (i in starViews.indices) {
            if (i < stars) {
                starViews[i].setImageResource(R.drawable.ic_star_filled)
            } else {
                starViews[i].setImageResource(R.drawable.ic_star)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatDrivers.size
    }
}