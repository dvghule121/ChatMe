package com.example.chatme.dataClass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.MainActivity
import com.example.chatme.R
import com.example.chatme.fragments.user_view
import com.example.chatme.new_chat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [user_page.newInstance] factory method to
 * create an instance of this fragment.
 */
class user_page : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_user_page, container, false)
        val contact_list = view.findViewById<RecyclerView>(R.id.contacts)
        val userList = mutableListOf<User>()
        val adapter = userViewAdapter(requireActivity())
        view.findViewById<ImageButton>(R.id.user).setOnClickListener {
            (activity as MainActivity).change(user_view())
        }

        contact_list.adapter = adapter
        contact_list.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)


        FirebaseDatabase.getInstance().getReference().child("chats").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = ArrayList<User>()
                for (postSnapshot_chat in snapshot.children) {
                    Firebase.database.getReference("Users")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {



                                for (postSnapshot in snapshot.children) {
                                    val curUser = postSnapshot.getValue(User::class.java)
                                    if (curUser != null && curUser.uid != Firebase.auth.uid.toString()) {

                                        if (postSnapshot_chat.key.toString() == curUser.uid.toString() + Firebase.auth.uid.toString()) {
                                            tempList.add(curUser)
                                            Log.d("TAG", "onDataChange: userroom exiest")
                                        } else {
                                            Log.d("TAG", "onDataChange: userroom not exiest")
                                        }

                                    }
                                }
                                adapter.setData(tempList)
                                adapter.notifyDataSetChanged()

                            }


                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.toString())
            }

        })

        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.new_chat)
            .setOnClickListener {
                val act = activity as MainActivity
                act.change(new_chat())
            }


        return view
    }

    override fun onStart() {
        super.onStart()

        val auth = Firebase.auth

        val uid = auth.currentUser?.uid
        if (uid.isNullOrBlank()) {
            val activity: MainActivity = getActivity() as MainActivity
            val loginPage = login_page()
            activity.change(loginPage)
            Log.d("clicked", "clicked")
        } else {
            true

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment user_page.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            user_page().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}