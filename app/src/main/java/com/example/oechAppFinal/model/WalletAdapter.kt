package com.example.oechAppFinal.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oechAppFinal.R

class WalletAdapter(private val transactions: List<Transaction>, private val isDarkTheme: Boolean) :
    RecyclerView.Adapter<WalletAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutResId = if (isDarkTheme) {
            R.layout.item_transaction_dark
        } else {
            R.layout.item_transaction
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.amountTextView.text = transaction.amount
        holder.descriptionTextView.text = transaction.description
        holder.dateTextView.text = transaction.date

        if (transaction.isIncome) {
            holder.amountTextView.setTextColor(holder.itemView.context.getColor(R.color.green_success_color))
        } else {
            holder.amountTextView.setTextColor(holder.itemView.context.getColor(R.color.red_error_color))
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}