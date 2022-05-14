package com.example.chatme

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatme.adapter.UserAdapter
import com.example.chatme.dataClass.TextMessage
import com.example.chatme.dataClass.user_page
import com.example.chatme.fragments.user_view
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [chatwindow.newInstance] factory method to
 * create an instance of this fragment.
 */
class chatwindow : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chatwindow, container, false)
        val bundle = this.arguments
        val userName = bundle!!.getString("user_name")
        val userurl = bundle.getString("user_url")
        val a_list = bundle.getString("user_id")!!
        val curUser = Firebase.auth.uid.toString()

        view.findViewById<ImageButton>(R.id.back).setOnClickListener {
            val act =  activity as MainActivity
            act.change(user_page())
        }

        view.findViewById<ImageButton>(R.id.user).setOnClickListener {
            val act =  activity as MainActivity
            act.change(user_view())
        }

        view.findViewById<TextView>(R.id.username).text = userName
        Glide.with(view.context).load(userurl).into(view.findViewById(R.id.userprofile))

        val senderRoom = curUser + a_list
        val recieverRoom = a_list + curUser


        val msg_view = view.findViewById<RecyclerView>(R.id.chats)
        val msegList = mutableListOf<TextMessage>()
        val adapter = UserAdapter(msegList)
        msg_view.adapter = adapter
        msg_view.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                msegList.clear()
                for (postSnapshot in snapshot.children){
                    msegList.add(postSnapshot.getValue(TextMessage::class.java)!!)


                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        val text_msg = view.findViewById<EditText>(R.id.msg_text)
        view.findViewById<ImageButton>(R.id.send_btn).setOnClickListener {
            val out_msg = TextMessage(text_msg.text.toString(), curUser)
            FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").push().setValue(out_msg).addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference().child("chats").child(recieverRoom).child("messages").push().setValue(out_msg)

            }
            text_msg.setText("")
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment chatwindow.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            chatwindow().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}