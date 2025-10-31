package com.example.bt_password

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bt_password.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.btnGoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }
    }


    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }


    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Xác thực Firebase với tài khoản Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("name", user?.displayName)
                    intent.putExtra("email", user?.email)
                    intent.putExtra("photo", user?.photoUrl.toString())
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Firebase login failed", task.exception)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        // Nếu người dùng đã đăng nhập rồi → chuyển luôn sang Profile
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("name", currentUser.displayName)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("photo", currentUser.photoUrl.toString())
            startActivity(intent)
            finish()
        }
    }
}
