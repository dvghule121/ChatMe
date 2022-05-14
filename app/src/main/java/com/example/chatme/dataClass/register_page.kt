package com.example.chatme.dataClass

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.chatme.MainActivity
import com.example.chatme.R
import com.example.chatme.fragments.user_view
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [register_page.newInstance] factory method to
 * create an instance of this fragment.
 */
class register_page : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val REQUEST_CODE = 100
    private var imageLink: String? = null
    private var imageUri:Uri? = null

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
        val view = inflater.inflate(R.layout.fragment_register_page, container, false)
        val username = view.findViewById<EditText>(R.id.user_name_et)
        val usermobile = view.findViewById<EditText>(R.id.user_mobile_et)
        val useraddress = view.findViewById<EditText>(R.id.user_address_et)
        val password = view.findViewById<EditText>(R.id.user_pass_et)
        val createAccount = view.findViewById<Button>(R.id.create_button)

        view.findViewById<ImageButton>(R.id.back).setOnClickListener {
            val act =  activity as MainActivity
            act.change(user_page())
        }

        view.findViewById<ImageButton>(R.id.user).setOnClickListener {
            val act =  activity as MainActivity
            act.change(user_view())
        }

        view.findViewById<Button>(R.id.select_profile).setOnClickListener {
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

        createAccount.setOnClickListener {
            val auth = FirebaseAuth.getInstance()

            if (TextUtils.isEmpty(username.text.toString()) || TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(
                    context,
                    "Please enter email id and password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (imageUri != null) {

                    val imgId = Random.nextInt(10000)
                    val url = FirebaseStorage.getInstance().getReference("/images/$imgId.jpg")
                    url.putFile(imageUri!!)
                        .addOnSuccessListener {

                            val result = it.metadata!!.reference!!.downloadUrl;
                            result.addOnSuccessListener {

                                imageLink = it.toString()
                                if ( imageLink == null){
                                    Toast.makeText(
                                        context,
                                        "Please choose profile image",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else {
                                    val activity = activity as MainActivity

                                    activity.register_user(
                                        username.text.toString(),
                                        password.text.toString(),
                                        usermobile.text.toString(),
                                        useraddress.text.toString(),
                                        imageLink!!
                                    )
                                    Toast.makeText(
                                        context,
                                        "successfully registered",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }


                            }
                        }
                }
                else{
                    Toast.makeText(
                        context,
                        "Please choose profile image",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }


        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            imageUri = data?.data
            view?.findViewById<ImageView>(R.id.profile_img_reg)?.setImageURI(imageUri)

        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment register_page.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            register_page().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}