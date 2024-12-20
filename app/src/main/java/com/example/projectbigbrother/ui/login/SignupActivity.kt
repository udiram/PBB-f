package com.example.projectbigbrother.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectbigbrother.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

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
                Log.d(
                    "SignupActivity",
                    "Sign-up button clicked with data: $firstName $lastName $email"
                )
                signUpUser(firstName, lastName, email, password)
            } else {
                Log.d("SignupActivity", "Validation failed")
            }
        }
    }

    private fun validateInputs(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Boolean {
        if (TextUtils.isEmpty(firstName)) {
            firstNameField.error = "First name is required"
            return false
        }
        if (TextUtils.isEmpty(lastName)) {
            lastNameField.error = "Last name is required"
            return false
        }
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

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // Log the error
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Failed to sign up: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string() ?: "No response body"
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Sign-up successful!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
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
}
