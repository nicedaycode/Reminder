package com.example.reminder.fragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.reminder.activity.RestockMedicineActivity
import com.example.reminder.activity.SignIn
import com.example.reminder.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SettingFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var storageRef: FirebaseStorage
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storageRef = FirebaseStorage.getInstance()

        loadUserData()

        binding.logout.setOnClickListener { showLogoutDialog() }

        binding.restoreMedicine.setOnClickListener {
            startActivity(Intent(requireContext(), RestockMedicineActivity::class.java))
        }

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { result ->
            if (result != null) {
                binding.profileImg.setImageURI(result)
                uri = result
                updateProfile()
            }
        }

        binding.profileImg.setOnClickListener {
            galleryImage.launch("image/*")
        }
    }

    private fun loadUserData() {
        val uid = firebaseAuth.currentUser?.uid.orEmpty()
        database.reference.child("users").child(uid)
            .get().addOnSuccessListener { snapshot ->
                snapshot.child("name").value?.let {
                    binding.userNameTxt.text = "반갑습니다 $it!"
                }
                val imageURL = snapshot.child("imageURL").value as String?
                if (!imageURL.isNullOrEmpty() && imageURL != "null") {
                    Glide.with(requireContext()).load(imageURL).into(binding.profileImg)
                }
            }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("로그아웃?")
            setMessage("로그아웃 하시겠습니까?")
            setPositiveButton("YES") { _, _ -> logoutUser() }
            setNegativeButton("No", null)
            show()
        }
    }

    private fun logoutUser() {
        firebaseAuth.signOut()
        requireContext().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE).edit().apply {
            putBoolean("flag", false)
            apply()
        }
        startActivity(Intent(requireContext(), SignIn::class.java))
        activity?.finish()
    }

    private fun updateProfile() {
        uri?.let { uri ->
            val uid = firebaseAuth.currentUser?.uid.orEmpty()
            val reference = storageRef.reference.child("Profile/$uid")
            reference.putFile(uri).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    database.reference.child("users").child(uid)
                        .updateChildren(mapOf("imageURL" to imageUrl)).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Profile updated successfully.", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
