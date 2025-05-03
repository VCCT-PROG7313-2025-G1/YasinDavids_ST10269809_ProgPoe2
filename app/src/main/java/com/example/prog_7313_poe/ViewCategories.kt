package com.example.prog_7313_poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.prog_7313_poe.data.AppDatabase
import kotlinx.coroutines.launch

class ViewCategories : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        return inflater.inflate(R.layout.fragment_view_cat, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val billsContainer = view.findViewById<LinearLayout>(R.id.billsContainer)
        val db = AppDatabase.getDatabase(requireContext())
        val categoryDao = db.categoryDao()

        lifecycleScope.launch {
            val categories = categoryDao.getAllCategories() // or your equivalent DAO method
            for (category in categories) {

                if (category.section == "Bills") {

                    //Label for bar
                    val textView = TextView(requireContext()).apply {
                        text = "${category.name} "
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.white
                            )
                        )
                        textSize = 16f
                        setPadding(0, 8, 0, 8)
                    }

                    // Bar
                    val progressBar = ProgressBar(
                        requireContext(),
                        null,
                        android.R.attr.progressBarStyleHorizontal
                    ).apply {
                        max = category.goal.toInt()  // Total goal value
                        progress = ((category.goal_progress / category.goal) * max).toInt() // Current progress
                        progressDrawable = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.custom_progress_bar
                        )
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            24
                        ).apply {
                            topMargin = 4
                            bottomMargin = 16
                        }
                    }
                    billsContainer.addView(textView)
                    billsContainer.addView(progressBar)
                }
            }
        }
    }
}