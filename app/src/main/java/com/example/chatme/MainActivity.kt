package com.example.chatme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.chatme.dataClass.User
import com.example.chatme.dataClass.register_page
import com.example.chatme.dataClass.user_page
import com.example.chatme.fragments.user_view
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*





class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        change(user_page())





    }




    fun change(toFragment: Fragment) {
        val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager
        val transaction: FragmentTransaction =
            manager.beginTransaction() //create an instance of Fragment-transaction
        transaction.replace(R.id.main_view, toFragment, "Frag_Top_tag")
        transaction.commit()
    }

    fun login_user(email: String, pass: String) {

        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Log in successfully", Toast.LENGTH_SHORT).show()
                change(user_page())
            } else {
                Toast.makeText(this, "Log in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun register_user(
        email: String,
        pass: String,
        mobile: String,
        name: String,
        profileImg:String
    ) {
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(
            email,
            pass
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val uid = FirebaseAuth.getInstance().uid
                Toast.makeText(this, uid, Toast.LENGTH_SHORT).show()
//                Log.d("TAG", "register_user: "+ user.name)
                val reference = FirebaseDatabase.getInstance().getReference().child("Users").child(
                    uid.toString()
                ).setValue(User(email,uid.toString(),mobile, name, imgUrl = profileImg))
                change(user_page())

            } else {
                Toast.makeText(
                    this,
                    "registration failed, Please try again after some time",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(
                    "error_msg",
                    "onComplete: Failed=" + Objects.requireNonNull(task.exception)!!.message
                )
            }
        }
    }

}