package com.example.assignment_6

import android.os.Bundle

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var mainExpenseName: EditText
    private lateinit var mainExpenseAmt: EditText
    private lateinit var btAdd: Button
    private lateinit var recyclerView: RecyclerView
    private val listExpense = mutableListOf<Pair<String, String>>()
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainExpenseName = findViewById(R.id.mainExpenseName)
        mainExpenseAmt = findViewById(R.id.mainExpenseAmt)
        btAdd = findViewById(R.id.btAdd)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this) //used layout manager to put list items easily
        expenseAdapter = ExpenseAdapter()
        recyclerView.adapter = expenseAdapter
                btAdd.setOnClickListener {
                    val name = mainExpenseName.text.toString().trim()         //taking name string
                    val amount = mainExpenseAmt.text.toString().trim()     //taking amount number

                    if (name.isNotEmpty() && amount.isNotEmpty()) {
                        listExpense.add(Pair(name, amount))
                        expenseAdapter.notifyItemInserted(listExpense.size - 1)
                        mainExpenseName.text.clear()
                        mainExpenseAmt.text.clear()
                    } else {
                        Toast.makeText(
                            this, "Please enter both in above boxes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
