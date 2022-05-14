package com.example.chatme.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.chatme.MainActivity
import com.example.chatme.R
import com.example.chatme.dataClass.User
import com.example.chatme.dataClass.login_page
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.chatme.dataClass.user_page
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlin.random.Random


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [user_view.newInstance] factory method to
 * create an instance of this fragment.
 */
class user_view : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val REQUEST_CODE = 100
    var imageUri :Uri? = null

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
        val view = inflater.inflate(R.layout.fragment_user_view, container, false)
        val img_btn = view.findViewById<ImageButton>(R.id.choose_btn)
        view.findViewById<ImageButton>(R.id.back).setOnClickListener {
            val act =  activity as MainActivity
            act.change(user_page())
        }

        view.findViewById<ImageButton>(R.id.user).setOnClickListener {
            val act =  activity as MainActivity
            act.change(user_view())
        }


        img_btn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            intent.putExtra("scale", true)
            intent.putExtra("outputX", 256)
            intent.putExtra("outputY", 256)
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("return-data", true)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, REQUEST_CODE)


        }







        FirebaseDatabase.getInstance().getReference().child("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val user = i.value as HashMap<String,String>
                        Log.d("TAG", "onDataChange:${user.get("uid")}+${Firebase.auth.uid.toString()} ")
                        if (user.get("uid") == Firebase.auth.uid.toString()) {
                            Log.d("TAG", "onDataChange:${user.get("uid")} ")

                            view.findViewById<TextView>(R.id.user_view_name).text = user["name"]
                            view.findViewById<TextView>(R.id.user_view_mobile).text = user["mobile"]
                            view.findViewById<TextView>(R.id.user_view_email).text  = user["email"]

//                            Log.d("ImgUrl", imgUrl.toString()+" "+user["imgUrl"].toString())
                            Glide.with(view.context).load(user["imgUrl"]).into(view.findViewById<ImageView>(R.id.userImg))

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        view.findViewById<Button>(R.id.logout).setOnClickListener {
            Firebase.auth.signOut()
            (activity as MainActivity).change(login_page())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){

            imageUri = data?.data
            requireView().findViewById<ImageView>(R.id.profile_img)?.setImageURI(imageUri)
            val imgId = Random.nextInt(10000)
            val url = FirebaseStorage.getInstance().getReference("/images/$imgId.jpg")

            url.putFile(imageUri!!)
                .addOnSuccessListener {

                    val result = it.metadata!!.reference!!.downloadUrl;
                    result.addOnSuccessListener {

                        var imageLink = it.toString()
                        FirebaseDatabase.getInstance().getReference().child("Users").child(Firebase.auth.uid.toString()).child("imgUrl").setValue(imageLink)// handle chosen image

                    }
                }



        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment user_view.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            user_view().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}