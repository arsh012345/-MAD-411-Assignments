package com.example.assignment_6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FooterFragment : Fragment() {

    private lateinit var TotalExpnse: TextView         //created variables here
    private var totalExpense = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_footer, container, false)
        TotalExpnse = view.findViewById(R.id.totalExpenses)
        return view
    }

    fun updateTotalExpense(newTotal: Double) {           //function to find total
        totalExpense = newTotal
        TotalExpnse.text = "Total Expenses: $$totalExpense"      //putting total here
    }
}