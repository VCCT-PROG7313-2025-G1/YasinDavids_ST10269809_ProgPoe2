package com.example.prog_7313_poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.prog_7313_poe.data.Account
import com.example.prog_7313_poe.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddAccount : Fragment() {

    private lateinit var spinner: Spinner
    private lateinit var edtName: EditText
    private lateinit var edtAmount: EditText
    private lateinit var edtNotes: EditText
    private lateinit var btnAdd: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_account, container, false)

        spinner = view.findViewById(R.id.accountTypeSelect)
        edtName = view.findViewById(R.id.edt_accountName)
        edtAmount = view.findViewById(R.id.edt_startingAmount)
        edtNotes = view.findViewById(R.id.edt_notes)
        btnAdd = view.findViewById(R.id.btn_confirmAccount)

        val options = listOf("Bank", "Credit", "Cash")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        btnAdd.setOnClickListener {
            val type = spinner.selectedItem.toString()
            val name = edtName.text.toString().trim()
            val amount = edtAmount.text.toString().toDoubleOrNull()
            val notes = edtNotes.text.toString().trim()

            if (name.isEmpty() || amount == null) {
                Toast.makeText(requireContext(), "Please enter a valid name and amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val account = Account(
                type = type,
                name = name,
                startingAmount = amount,
                notes = if (notes.isEmpty()) null else notes
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    AppDatabase.getDatabase(requireContext()).accountDao().insert(account)
                }
                Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
                edtName.text.clear()
                edtAmount.text.clear()
                edtNotes.text.clear()
            }
        }

        return view
    }
}
