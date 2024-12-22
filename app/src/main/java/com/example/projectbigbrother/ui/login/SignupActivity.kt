package com.example.projectbigbrother.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectbigbrother.MainActivity
import com.example.projectbigbrother.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class SignUpActivity : AppCompatActivity() {

    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var signUpButton: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Bind views
        firstNameField = findViewById(R.id.first_name)
        lastNameField = findViewById(R.id.last_name)
        emailField = findViewById(R.id.email)
        passwordField = findViewById(R.id.password)
        signUpButton = findViewById(R.id.signup_button)

        signUpButton.setOnClickListener {
            val firstName = firstNameField.text.toString().trim()
            val lastName = lastNameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (validateInputs(firstName, lastName, email, password)) {
                Log.d("SignUpActivity", "Sign-up button clicked with data: $firstName $lastName $email")
                signUpUser(firstName, lastName, email, password)
            } else {
                Log.d("SignUpActivity", "Validation failed")
            }
        }
    }

    private fun validateInputs(firstName: String, lastName: String, email: String, password: String): Boolean {
        if (firstName.isEmpty()) {
            firstNameField.error = "First name is required"
            return false
        }
        if (lastName.isEmpty()) {
            lastNameField.error = "Last name is required"
            return false
        }
        if (email.isEmpty()) {
            emailField.error = "Email is required"
            return false
        }
        if (password.isEmpty()) {
            passwordField.error = "Password is required"
            return false
        }
        return true
    }

    private fun signUpUser(firstName: String, lastName: String, email: String, password: String) {
        val url = "http://10.0.2.2:5000/users" // Replace with your backend URL
        val json = JSONObject()
        json.put("first_name", firstName)
        json.put("last_name", lastName)
        json.put("email", email)
        json.put("password", password)

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            json.toString()
        )

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SignUpActivity", "Network request failed: ${e.message}")
                runOnUiThread {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Failed to connect to server: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: "No response body"
                Log.d("SignUpActivity", "Response: $responseBody")

                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Sign-up failed: ${response.code} $responseBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity to prevent navigating back to it
    }
}
