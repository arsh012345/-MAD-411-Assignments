package com.example.assignment_6


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : AppCompatActivity() {
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

        val name = intent.getStringExtra("expenseName") ?: "no idea"
        val amount = intent.getStringExtra("expenseAmount") ?: "0.00"     //adding details if avaleble
        val date = intent.getStringExtra("expenseDate") ?: "no idea"

        dName.text = "Name of expense: $name"
        dAmount.text = "Amount: $amount"
        dDate.text = "Date: $date"

        btBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)  //intent to home location of app
            startActivity(intent)
            finish()
        }
    }
}