package com.example.assignment_6

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class ExpenseDetailsFragment : Fragment() {
    private lateinit var dName: TextView
    private lateinit var dAmount: TextView
    private lateinit var dDate: TextView          //putting variables here
    private lateinit var btBack: Button

    private val arguts: ExpenseDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_expense_details, container, false)

        dName = view.findViewById(R.id.detailExpenseName)
        dAmount = view.findViewById(R.id.detailExpenseAmount)
        dDate = view.findViewById(R.id.detailExpenseDate)
        btBack = view.findViewById(R.id.btBackHome)

        dName.text = "Name of expense: ${arguts.name}"
        dAmount.text = "Amount: ${arguts.amount}"
        dDate.text = "Date: ${arguts.date}"

        btBack.setOnClickListener {
            findNavController().navigateUp()
        }
        return view
    }
}
