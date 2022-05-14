package com.example.chatme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.R
import com.example.chatme.dataClass.TextMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserAdapter(val messageList: List<TextMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            val view: View = inflater.inflate(R.layout.sentmsg, parent, false)
            return sentViewHolder(view)
        }
        else{
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            val view: View = inflater.inflate(R.layout.recievemsg, parent, false)
            return recieveViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (currentMessage.senderId == Firebase.auth.uid.toString()){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == sentViewHolder::class.java){
            // sentview holder
            val currentMessage = messageList[position]
            holder as sentViewHolder
            holder.sentmsg.text = currentMessage.text

        }
        else{
            // recieve holder
            val currentMessage = messageList[position]
            holder as recieveViewHolder
            holder.recievemsg.text = currentMessage.text

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class sentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentmsg = itemView.findViewById<TextView>(R.id.sent_msg)
    }

    class recieveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val recievemsg = itemView.findViewById<TextView>(R.id.recieve)

    }
}