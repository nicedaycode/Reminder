
package com.example.reminder.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.reminder.R
import com.example.reminder.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
    private var uri: Uri? = null
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val storageRef: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private var imageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { onBackPressed() }

        fetchUserData()

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.profileImg.setImageURI(it)
                this.uri = it
                uploadImageToFirebase()
            }
        }

        binding.profileImg.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.updateBtn.setOnClickListener {
            updateProfile()
        }
    }

    private fun fetchUserData() {
        firebaseAuth.currentUser?.uid?.let { uid ->
            database.reference.child("users").child(uid).get().addOnSuccessListener { dataSnapshot ->
                val name = dataSnapshot.child("name").value.toString()
                val email = dataSnapshot.child("email").value.toString()
                val phoneNumber = dataSnapshot.child("phoneNumber").value.toString()
                val address = dataSnapshot.child("address").value.toString()
                val imageURL = dataSnapshot.child("imageURL").value.toString()

                binding.apply {
                    userNameTxt.text = name
                    nameEdt.setText(name)
                    userEmailTxt.text = email
                    emailEdt.setText(email)
                    phoneNoEdt.setText(phoneNumber)
                    addressEdt.setText(address)
                }

                if (imageURL != "null") {
                    Glide.with(this@JoinActivity).load(imageURL).into(binding.profileImg)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "정보를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToFirebase() {
        uri?.let { uri ->
            val ref = storageRef.reference.child("Profile/${firebaseAuth.currentUser?.uid}")
            ref.putFile(uri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageUrl = task.result.toString()
                    Toast.makeText(this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateProfile() {
        val userMap = mapOf(
            "userId" to firebaseAuth.currentUser?.uid,
            "name" to binding.nameEdt.text.toString(),
            "imageURL" to imageUrl,
            "email" to binding.emailEdt.text.toString(),
            "phoneNumber" to binding.phoneNoEdt.text.toString(),
            "address" to binding.addressEdt.text.toString()
        )

        firebaseAuth.currentUser?.uid?.let { uid ->
            database.reference.child("users").child(uid).updateChildren(userMap).addOnSuccessListener {
                Toast.makeText(this, "프로필 업데이트 성공", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "프로필 업데이트 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}