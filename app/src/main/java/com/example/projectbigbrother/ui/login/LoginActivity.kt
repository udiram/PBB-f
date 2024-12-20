package com.example.projectbigbrother.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectbigbrother.MainActivity
import com.example.projectbigbrother.R
import com.example.projectbigbrother.ui.login.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var forgotPasswordText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Bind views
        emailField = findViewById(R.id.email)
        passwordField = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        signUpButton = findViewById(R.id.signup_button)
        forgotPasswordText = findViewById(R.id.forgot_password)

        // Set click listeners
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (validateInputs(email, password)) {
                loginUser(email, password)
            }
        }

        signUpButton.setOnClickListener {
            // Navigate to Sign Up Activity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordText.setOnClickListener {
            // Handle forgot password functionality (Optional)
            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            emailField.error = "Email is required"
            return false
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.error = "Password is required"
            return false
        }
        return true
    }

    private fun loginUser(email: String, password: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val storedPassword = sharedPreferences.getString(email, null)

        if (storedPassword != null && storedPassword == password) {
            // Login success, navigate to the main activity
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // If login fails, display a message to the user
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show()
        }
    }
}
