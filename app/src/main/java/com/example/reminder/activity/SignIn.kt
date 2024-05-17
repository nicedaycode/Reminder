package com.example.reminder.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.reminder.R
import com.example.reminder.databinding.ActivitySignInBinding
import com.example.reminder.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class SignIn : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.signUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        binding.signInBtn.setOnClickListener {
            val email = binding.emailEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString().trim()
            if (validateForm(email, password)) {
                signInWithEmailAndPassword(email, password)
            }
            // Reset input fields
            binding.emailEdt.text = null
            binding.passwordEdt.text = null
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleSignUpBtn.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        var valid = true
        if (email.isEmpty()) {
            binding.emailEdt.error = "이메일을 입력해주세요."
            valid = false
        }
        if (password.isEmpty()) {
            binding.passwordEdt.error = "비밀번호를 입력해주세요."
            valid = false
        }
        return valid
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        val progressDialog = ProgressDialog(this@SignIn).apply {
            setTitle("잠시만 기다려주세요..")
            setMessage("앱을 로딩하는 중입니다. 잠시만 기다려주세요")
            show()
        }
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                handleSignInSuccess()
            } else {
                showToast("로그인 실패: ${task.exception?.localizedMessage}")
            }
            progressDialog.dismiss()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleGoogleSignInResult(task)
            }
        }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account = task.result
            account?.let {
                signInWithGoogleAccount(it)
            }
        } else {
            showToast("Google 로그인 실패: ${task.exception?.localizedMessage}")
        }
    }

    private fun signInWithGoogleAccount(account: GoogleSignInAccount) {
        val progressDialog = ProgressDialog(this@SignIn).apply {
            setTitle("잠시만 기다려주세요..")
            setMessage("앱을 로딩하는 중입니다. 잠시만 기다려주세요")
            show()
        }
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                user?.let {
                    saveUserToDatabase(
                        it.uid,
                        account.displayName ?: "",
                        account.email ?: "",
                        account.photoUrl.toString()
                    )
                }
            } else {
                showToast("Google 계정으로 로그인 실패: ${task.exception?.localizedMessage}")
            }
            progressDialog.dismiss()
        }
    }

    private fun saveUserToDatabase(userId: String, name: String, email: String, imageURL: String) {
        val user = User(userId, name, email, imageURL)
        database.reference.child("users").child(userId).setValue(user)
    }

    private fun handleSignInSuccess() {
        val intent = Intent(this, TapActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
