package com.example.assignment_6


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : Fragment() {
    private lateinit var dName: TextView
    private lateinit var dAmount: TextView                   //putting variable here
    private lateinit var dDate: TextView
    private lateinit var btBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        dName = findViewById(R.id.detailExpenseName)
        dAmount = findViewById(R.id.detailExpenseAmount)
        dDate = findViewById(R.id.detailExpenseDate)
        btBack = findViewById(R.id.btBackHome)

        dName.text = "Name of expense: $name"
        dAmount.text = "Amount: $amount"
        dDate.text = "Date: $date"

        btBack.setOnClickListener {
            //intent to home location of app

        }
        return view
    }
}