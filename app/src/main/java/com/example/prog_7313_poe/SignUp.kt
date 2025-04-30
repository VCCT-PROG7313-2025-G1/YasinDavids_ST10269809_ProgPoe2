package com.example.prog_7313_poe

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.prog_7313_poe.data.AppDatabase
import com.example.prog_7313_poe.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUp : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var createAccountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        nameEditText = findViewById(R.id.edt_Fname)
        surnameEditText = findViewById(R.id.edt_Lname)
        emailEditText = findViewById(R.id.edt_Email)
        passwordEditText = findViewById(R.id.edt_Pass)
        createAccountButton = findViewById(R.id.btn_Caccount)

        val db = AppDatabase.getDatabase(this)

        createAccountButton.setOnClickListener()
        {
            val name = nameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Simple validation
            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(name = name, surname = surname, email = email, password = password)

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        db.userDao().insertUser(user)
                    }

                    Toast.makeText(this@SignUp, "Account created successfully", Toast.LENGTH_SHORT)
                        .show()

                    // Go back to MainActivity
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<FloatingActionButton>(R.id.btn_Back)
        backButton.setOnClickListener {
            finish()
        }
    }
}