package com.example.prog_7313_poe

// imports
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prog_7313_poe.data.AppDatabase
import com.example.prog_7313_poe.databinding.FragmentAddTransactionBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddTransaction : Fragment(R.layout.fragment_add_transaction) {
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!




    // --------------------------------------------------------------------

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {

                Toast.makeText(requireContext(), "Image captured successfully", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private val CAMERA_PERMISSION_CODE = 100

    // Add this function to check and request permissions
    private fun checkCameraPermission() {
        if (requireContext().checkSelfPermission(android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            // Permission already granted, open camera
            openCamera()
        }
    }

    // Handle the permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open camera
                openCamera()
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to open camera
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureLauncher.launch(takePictureIntent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddTransactionBinding.bind(view)

        // --------------- CATEGORY SPINNER ---------------------

        val categorySpinner = view.findViewById<Spinner>(R.id.categorySelect)

        lifecycleScope.launch {
            val categoryDao = AppDatabase.getDatabase(requireContext()).categoryDao()
            val categoryNames = categoryDao.getAllCategoryNames()

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        // SAVE BUTTON ACTION
        val saveButton = view.findViewById<Button>(R.id.save_income_category_btn)
        saveButton.setOnClickListener {
            val incomeAmount = binding.incomeAmount.text.toString().toDoubleOrNull() // Get income amount
            if (incomeAmount != null) {
                val selectedCategory = categorySpinner.selectedItem.toString() // Get selected category

                // Insert into goal_progress table
                lifecycleScope.launch {
                    val categoryDao = AppDatabase.getDatabase(requireContext()).categoryDao()

                    // Get the category by name
                    val category = categoryDao.getCategoryByName(selectedCategory)
                    if (category != null) {
                        // Update goal progress for the selected category
                        categoryDao.updateGoalProgress(selectedCategory, incomeAmount)
                        Toast.makeText(requireContext(), "Goal progress updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Category not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a valid income amount", Toast.LENGTH_SHORT).show()
            }
        }


        // --------------------------- PHOTO BUTTON _----------

        val takePhotoBtn = view.findViewById<Button>(R.id.btnTakePhoto)

        takePhotoBtn.setOnClickListener {
            checkCameraPermission() // Use the permission check method instead of directly launching
        }

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