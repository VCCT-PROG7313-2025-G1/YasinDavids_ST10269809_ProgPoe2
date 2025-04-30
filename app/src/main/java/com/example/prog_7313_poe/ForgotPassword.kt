package com.example.prog_7313_poe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.prog_7313_poe.data.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPassword : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var btnProceed: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtEmail = findViewById(R.id.edt_Emailchange)
        btnProceed = findViewById(R.id.btn_Next)

        val backButton = findViewById<FloatingActionButton>(R.id.btn_Back)
        backButton.setOnClickListener {
            finish()
        }

        btnProceed.setOnClickListener {
            val emailInput = edtEmail.text.toString().trim()

            if (emailInput.isEmpty()) {
                Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val userDao = AppDatabase.getDatabase(applicationContext).userDao()
                val user = userDao.getUserByEmail(emailInput)

                withContext(Dispatchers.Main) {
                    if (user == null) {
                        Toast.makeText(this@ForgotPassword, "Email does not have an account", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this@ForgotPassword, ResetPassword::class.java)
                        intent.putExtra("email", emailInput)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
