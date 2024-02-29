package com.example.oechAppFinal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oechAppFinal.R

class MenuItemAdapter(private val listener: Listener) : RecyclerView.Adapter<MenuItemAdapter.MenuItemHolder>() {

    var dark: Boolean = false

    private var itemsList = ArrayList<MenuItem>()

    class MenuItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var title: TextView = view.findViewById<TextView>(R.id.tvItemTitle)
        private var description: TextView = view.findViewById<TextView>(R.id.tvItemDescrip)
        private val icon: ImageView = view.findViewById<ImageView>(R.id.ivItemIcon)

        fun bind(position: Int, item: MenuItem, listener: Listener) {
            title.text = item.title
            description.text = item.description
            icon.setImageResource(item.icon)
            itemView.setOnClickListener {
                listener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemHolder {
        val view: View
        if (dark) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item_dark, parent, false)
        }
        else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        }

        return MenuItemHolder(view)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: MenuItemHolder, position: Int) {
        holder.bind(position, itemsList[position], listener)
    }

    fun addMenuItem(f: MenuItem) {
        itemsList.add(f)
        notifyDataSetChanged()
    }

    fun addAll(list: List<MenuItem>) {
        itemsList.clear()
        itemsList.addAll(list)
        notifyDataSetChanged()
    }

    interface Listener {
        fun onClick(id: Int)
    }
}