package com.example.prog_7313_poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.prog_7313_poe.data.AppDatabase
import com.example.prog_7313_poe.data.Category
import kotlinx.coroutines.launch

class AddCategory : Fragment() {



    override fun onCreateView(


        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEditText = view.findViewById<EditText>(R.id.category_name)
        val sectionSpinner = view.findViewById<Spinner>(R.id.accountSelect)
        val goalEditText = view.findViewById<EditText>(R.id.category_goal)
        val saveButton = view.findViewById<Button>(R.id.save_category_btn)

        val db = AppDatabase.getDatabase(requireContext())
        val categoryDao = db.categoryDao()

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.section_options,  // This is the array from strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sectionSpinner.adapter = adapter  // Sets the adapter to the spinner
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val section = sectionSpinner.selectedItem?.toString() ?: ""
            val goalText = goalEditText.text.toString().trim()

            if (name.isEmpty() || goalText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val goal = goalText.toDoubleOrNull()
            if (goal == null) {
                Toast.makeText(requireContext(), "Invalid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newCategory = Category(name = name, section = section, goal = goal, goal_progress = 0.0)

            lifecycleScope.launch {
                categoryDao.insertCategory(newCategory)
                Toast.makeText(requireContext(), "Category saved!", Toast.LENGTH_SHORT).show()
                // Optionally clear fields or navigate back
            }
        }
    }


}
