package com.example.localmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val list: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int) =
        if (list[position].isUser) 1 else 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user, parent, false)
            UserVH(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bot, parent, false)
            BotVH(view)
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = list[position]

        if (holder is UserVH) holder.text.text = msg.text
        else if (holder is BotVH) holder.text.text = msg.text
    }

    class UserVH(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.msgText)
    }

    class BotVH(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.msgText)
    }
}