package com.example.assignment_6.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.assignment_6.R

class ExpenseDetailsFragment : Fragment(){
    private lateinit var dName: TextView
    private lateinit var dAmount: TextView
    private lateinit var dDate: TextView          //putting variables here
    private lateinit var dConverted: TextView
    private lateinit var btBack: Button

    private val arguts: ExpenseDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_expense_details, container, false)

        dName = view.findViewById(R.id.detailExpenseName)
        dAmount = view.findViewById(R.id.detailExpenseAmount)
        dConverted = view.findViewById(R.id.detailConvertedCost)
        dDate = view.findViewById(R.id.detailExpenseDate)
        btBack = view.findViewById(R.id.btBackHome)

        dName.text = "Name:- ${arguts.name}"
        dAmount.text = "Amount:- ${arguts.amount} ${arguts.currency}"
        dDate.text = "Date:- ${arguts.date}"
        if(arguts.currency != "CAD"){        //if currency is not CAD than it show text
            dConverted.text = "Converted:- %.2f CAD".format(arguts.convertedCost)
            dConverted.isVisible = true
        }else
        {
            dConverted.text = ""
            dConverted.isVisible = false
        }
        btBack.setOnClickListener {
            findNavController().navigateUp()
        }
        return view
    }
}
