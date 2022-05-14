package com.example.chatme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatme.dataClass.User
import com.example.chatme.dataClass.userViewAdapter
import com.example.chatme.dataClass.user_page
import com.example.chatme.fragments.user_view
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [new_chat.newInstance] factory method to
 * create an instance of this fragment.
 */
class new_chat : Fragment() {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_chat, container, false)
        val contact_list = view.findViewById<RecyclerView>(R.id.contacts)
        val userList = mutableListOf<User>()
        val adapter = userViewAdapter(requireActivity())
        contact_list.adapter = adapter
        contact_list.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL, false
        )

        view.findViewById<ImageButton>(R.id.back).setOnClickListener {
            val act =  activity as MainActivity
            act.change(user_page())
        }

        view.findViewById<ImageButton>(R.id.user).setOnClickListener {
            val act =  activity as MainActivity
            act.change(user_view())
        }


        val searchbar = view.findViewById<SearchView>(R.id.search_bar)



        Firebase.database.getReference("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val curUser = postSnapshot.getValue(User::class.java)
                    if (curUser != null && curUser.uid != Firebase.auth.uid.toString()) {
                        userList.add(curUser)

                    }
                    searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            searchbar.clearFocus()
                            val users = mutableListOf<User>()
                            for (i in userList) {
                                if (query.toString() in i.name.toString().lowercase()) {
                                    users.add(i)
                                }
                            }
                            adapter.setData(users as ArrayList<User>)
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            val users = mutableListOf<User>()
                            for (i in userList) {
                                if (newText.toString() in i.name.toString().lowercase()) {
                                    users.add(i)
                                }
                            }
                            adapter.setData(users as ArrayList<User>)
                            return false
                        }
                    })
                    adapter.setData(userList as ArrayList<User>)
                    adapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment new_chat.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            new_chat().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}