package com.example.minimezun

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.minimezun.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        val  nameText: TextView = view.findViewById(R.id.profileName)
        val lastNameText:TextView = view.findViewById(R.id.profileLastname)
        val bolumText:TextView = view.findViewById(R.id.profileBolum)
        val mezuniyet: TextView = view.findViewById(R.id.profileMezuniyet)
        val sImage :ImageView = view.findViewById(R.id.profileImg)
        val startDateText:TextView  = view.findViewById(R.id.profilGirisYili)
        val finishDateText :TextView = view.findViewById(R.id.profileMezunYili)
        val emailText:TextView  = view.findViewById(R.id.profilemail)

        val uID  =  Firebase.auth.currentUser!!.uid
        Log.d(TAG, "uid: $uID")
        val db = Firebase.firestore

        val storageRefForImage = FirebaseStorage.getInstance().reference.child("profileImages/$uID.jpg")
        val localImage = File.createTempFile("profileImg","jpg")

        db.collection("mezunlar").document(uID).get().addOnSuccessListener {
            Log.d(TAG, "DocumentSnapshot data: $it")
           if(it != null){
               val user = it.toObject<User>()
               if(user != null){
                   nameText.text = user.isim
                   lastNameText.text = user.soyisim
                   bolumText.text = user.bolum
                   mezuniyet.text = user.mezuniyet
                   emailText.text = user.email
                   startDateText.text = user.girisYili
                   finishDateText.text = user.mezunYili
                   storageRefForImage.getFile(localImage).addOnSuccessListener {
                       val bitmap = BitmapFactory.decodeFile(localImage.absolutePath)
                       sImage.setImageBitmap(bitmap)
                   }.addOnFailureListener{
                       sImage.setImageResource(R.drawable.ic_round_school_24)
                   }
               }else {
                   Log.d(TAG,"cekemedim")
               }
           }
        }.addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn = view.findViewById(R.id.logOutbtn)
        btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intentLogOutNow = Intent(activity, LoginActivity::class.java)
            this.startActivity(intentLogOutNow)
            activity?.finish()
        }
    }
}