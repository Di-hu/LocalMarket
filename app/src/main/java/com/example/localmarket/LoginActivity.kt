package com.example.localmarket

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEt = findViewById<EditText>(R.id.emailEt)
        val passwordEt = findViewById<EditText>(R.id.passwordEt)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val googleBtn = findViewById<LinearLayout>(R.id.googleBtn)
        val signupText = findViewById<TextView>(R.id.signupText)
        val loadingBar = findViewById<ProgressBar>(R.id.loadingBar)
        val togglePassword = findViewById<ImageView>(R.id.togglePassword)

        // 🔥 PASSWORD TOGGLE LOGIC
        togglePassword.setOnClickListener {

            if (passwordEt.inputType ==
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            ) {

                passwordEt.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

                togglePassword.setImageResource(R.drawable.ic_eye_off)

            } else {

                passwordEt.inputType =
                    InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD

                togglePassword.setImageResource(R.drawable.ic_eye)
            }

            passwordEt.setSelection(passwordEt.text.length)
        }

        // 🔐 EMAIL LOGIN
        loginBtn.setOnClickListener {

            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loadingBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    loadingBar.visibility = View.GONE

                    if (task.isSuccessful) {
                        goToDashboard()
                    } else {
                        Toast.makeText(
                            this,
                            task.exception?.message ?: "Login Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // 🔵 GOOGLE SIGN-IN SETUP
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(this, gso)

        googleBtn.setOnClickListener {

            googleClient.signOut().addOnCompleteListener {
                val signInIntent = googleClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }

        // 📝 SIGNUP CLICK
        signupText.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }
    }

    // 🔵 GOOGLE RESULT HANDLER
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                val credential =
                    GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            goToDashboard()
                        } else {
                            Toast.makeText(
                                this,
                                authTask.exception?.message ?: "Google Authentication Failed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            } catch (e: ApiException) {
                Log.e("GOOGLE_ERROR", "Error Code: ${e.statusCode}")
                Toast.makeText(
                    this,
                    "Google Sign In Failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun goToDashboard() {
        startActivity(Intent(this, DashBoardActivity::class.java))
        finish()
    }
}