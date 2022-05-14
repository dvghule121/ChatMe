package com.example.chatme.dataClass

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatme.MainActivity
import com.example.chatme.R
import com.example.chatme.chatwindow
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlin.math.log


class userViewAdapter(val activity: Activity) : RecyclerView.Adapter<userViewHolder>() {
    var orders = ArrayList<User>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.contact_card, parent, false)
        return userViewHolder(view)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {
        var lastmsg = ""
        val user = orders[position]
        FirebaseDatabase.getInstance().getReference().child("chats").child(user.uid.toString()+ Firebase.auth.uid
            .toString()).child("messages").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (postSnapshot in snapshot.children){
                    lastmsg = postSnapshot.getValue(TextMessage::class.java)!!.text.toString()
                }
                holder.lasttext.text = lastmsg.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        holder.textTitle.text = user.name
        Glide.with(holder.imgView.context).load(user.imgUrl).into(holder.imgView)

        holder.textTitle.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("user_id",user.uid.toString())
            bundle.putString("user_name", user.name.toString())
            bundle.putString("user_url", user.imgUrl.toString())
            val msg_view = chatwindow()
            msg_view.arguments = bundle
            val act = activity as MainActivity
            act.change(msg_view)


        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun setData(users:ArrayList<User>){

        this.orders = users
        notifyDataSetChanged()
    }


}

class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textTitle = itemView.findViewById<TextView>(R.id.contact_name)
    val lasttext = itemView.findViewById<TextView>(R.id.last_msg)
    val imgView = itemView.findViewById<ImageView>(R.id.profile_img)



}
