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

class ResetPassword : AppCompatActivity() {

    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnResetPassword: Button
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)

        // Layout padding for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get the email passed from ForgotPassword screen
        email = intent.getStringExtra("email") ?: ""

        // Initialize views
        edtNewPassword = findViewById(R.id.edt_newPass)
        edtConfirmPassword = findViewById(R.id.edt_confirmNewPass)
        btnResetPassword = findViewById(R.id.btn_Confirm)
        val backButton = findViewById<FloatingActionButton>(R.id.btn_Back)

        // Back button
        backButton.setOnClickListener {
            finish()
        }

        // Reset password logic
        btnResetPassword.setOnClickListener {
            val newPass = edtNewPassword.text.toString()
            val confirmPass = edtConfirmPassword.text.toString()

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Enter both fields", Toast.LENGTH_SHORT).show()
            } else if (newPass != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val userDao = AppDatabase.getDatabase(applicationContext).userDao()
                    userDao.updatePassword(email, newPass)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ResetPassword, "Password changed successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ResetPassword, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}
