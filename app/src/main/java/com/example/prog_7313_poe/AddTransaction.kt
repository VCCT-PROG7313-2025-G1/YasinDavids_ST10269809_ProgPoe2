package com.example.prog_7313_poe

// imports
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.prog_7313_poe.databinding.FragmentAddTransactionBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddTransaction : Fragment(R.layout.fragment_add_transaction) {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddTransactionBinding.bind(view)


        // ------------------------------- DATE INPUT --------------------------------------

        val incomeDateInput: EditText = binding.incomeDateInput

        // set date format
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        incomeDateInput.setOnClickListener {
            // Show calendar /  date picker
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                // stets the da`te in input box
                calendar.set(year, month, dayOfMonth)
                incomeDateInput.setText(dateFormat.format(calendar.time))
                (activity as? AppCompatActivity)?.supportActionBar?.hide() // hide the toolbar bc it keeps popping up for some reason
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // ---------------------------------- ACCOUNTS SPINNER -----------------------------------

        // Add arrays to spinner
        val spinner = binding.accountSelect

        // Get array from res/strings
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.account_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter  // Sets my array adapter to spinner
        }


        // -------------------------------- RADIO BUTTON SELECT -------------------------------------

        binding.incomeInputs.visibility = View.VISIBLE
        // Chooses what to display based on what radio group is selected
        binding.transactionTypeGroup.setOnCheckedChangeListener { _, checkedId ->


            when (checkedId) {
                // When income is selected
                R.id.rbIncome -> {
                    binding.incomeInputs.visibility = View.VISIBLE
                    binding.expenseInputs.visibility = View.GONE
                }
                // When expense is selected
                R.id.rbExpense -> {
                    binding.incomeInputs.visibility = View.GONE
                    binding.expenseInputs.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Clean up to avoid memory leaks
    }
}