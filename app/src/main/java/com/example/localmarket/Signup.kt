package com.example.localmarket

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var passwordVisible = false
    private var confirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val emailEt = findViewById<EditText>(R.id.emailEt)
        val passwordEt = findViewById<EditText>(R.id.passwordEt)
        val confirmPasswordEt = findViewById<EditText>(R.id.confirmPasswordEt)
        val signupBtn = findViewById<Button>(R.id.signupBtn)
        val loginText = findViewById<TextView>(R.id.loginText)
        val togglePassword = findViewById<ImageView>(R.id.togglePassword)
        val toggleConfirmPassword = findViewById<ImageView>(R.id.toggleConfirmPassword)
        val loadingBar = findViewById<ProgressBar>(R.id.loadingBar)

        // 🔐 PASSWORD TOGGLE
        togglePassword.setOnClickListener {
            passwordVisible = !passwordVisible

            if (passwordVisible) {
                passwordEt.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglePassword.setImageResource(R.drawable.ic_eye_off)
            } else {
                passwordEt.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePassword.setImageResource(R.drawable.ic_eye)
            }

            passwordEt.setSelection(passwordEt.text.length)
        }

        // 🔐 CONFIRM PASSWORD TOGGLE
        toggleConfirmPassword.setOnClickListener {
            confirmPasswordVisible = !confirmPasswordVisible

            if (confirmPasswordVisible) {
                confirmPasswordEt.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                toggleConfirmPassword.setImageResource(R.drawable.ic_eye_off)
            } else {
                confirmPasswordEt.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                toggleConfirmPassword.setImageResource(R.drawable.ic_eye)
            }

            confirmPasswordEt.setSelection(confirmPasswordEt.text.length)
        }

        // 🚀 SIGNUP LOGIC
        signupBtn.setOnClickListener {

            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()
            val confirmPassword = confirmPasswordEt.text.toString().trim()

            // Validation
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loadingBar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    loadingBar.visibility = View.GONE

                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Account Created Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Move directly to Dashboard
                        startActivity(Intent(this, DashBoardActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(
                            this,
                            task.exception?.message ?: "Signup Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // 🔙 Back to Login
        loginText.setOnClickListener {
            finish()
        }
    }
}