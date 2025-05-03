package com.example.prog_7313_poe

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(
            R.id.container,
            AddTransaction()
        ) // Replace with your fragment container ID and AddTransaction fragment
        transaction.commit()

        //ADD BUTTON LOGIC HERE
        val homeTextView = findViewById<TextView>(R.id.home_text_view)
        val transactionTextView = findViewById<TextView>(R.id.transaction_text_view)
        val categoryTextView = findViewById<TextView>(R.id.category_text_view)
        // Set an OnClickListener for the TextView
        homeTextView.setOnClickListener {
            // Replace current fragment with ViewCategories when the TextView is clicked
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.container,
                ViewCategories()
            )
            transaction.commit()

        }

        transactionTextView.setOnClickListener {
            // Replace current fragment with AddTransaction when the TextView is clicked
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.container,
                AddTransaction()
            )
            transaction.commit()

        }

        categoryTextView.setOnClickListener {
            // Replace current fragment with AddTransaction when the TextView is clicked
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.container,
                AddCategory()
            )
            transaction.commit()

        }

    }
}