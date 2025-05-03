package com.example.prog_7313_poe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.prog_7313_poe.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppSettings : Fragment() {

    private lateinit var btnDeleteAccount: Button
    private lateinit var btnLogOut: Button
    private lateinit var btnAboutUs: Button
    private lateinit var btnHowToUse: Button
    private lateinit var db: AppDatabase
    private lateinit var btnRefer: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_app_settings, container, false)

        btnDeleteAccount = view.findViewById(R.id.btn_deleteAccount)
        btnLogOut = view.findViewById(R.id.btn_logOut)
        btnAboutUs = view.findViewById(R.id.btn_aboutUs)
        btnHowToUse = view.findViewById(R.id.btn_howTo)

        db = AppDatabase.getDatabase(requireContext())

        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val currentUserEmail = sharedPreferences.getString("email", null)

        btnLogOut.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            Toast.makeText(requireContext(), "Successfully logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        btnDeleteAccount.setOnClickListener {
            currentUserEmail?.let { email ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        db.userDao().deleteUserByEmail(email)
                    }
                    sharedPreferences.edit().clear().apply()
                    Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        btnRefer = view.findViewById(R.id.btn_referUs)
        btnRefer.setOnClickListener {
            val link = "https://machamoney.app" // Or use your real app link
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("App Link", link)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(requireContext(), "Link copied", Toast.LENGTH_SHORT).show()
        }


        btnRefer.setOnClickListener {
            val link = "https://machamoney.app" // Or use your real app link
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("App Link", link)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(requireContext(), "Link copied", Toast.LENGTH_SHORT).show()
        }


        btnAboutUs.setOnClickListener {
            showInfoDialog(
                "About Us",
                "MachaMoney is an app that helps users manage their finances and achieve their savings goals."
            )
        }

        btnHowToUse.setOnClickListener {
            showInfoDialog(
                "How to Use MachaMoney",
                "1. Sign up or log in.\n2. Add your income and expenses.\n3. Set financial goals.\n4. Track progress and receive tips."
            )
        }

        return view
    }

    private fun showInfoDialog(title: String, message: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_about_us, null)

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<TextView>(R.id.dialog_title).text = title
        dialogView.findViewById<TextView>(R.id.dialog_message).text = message

        dialogView.findViewById<ImageView>(R.id.btn_close).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
