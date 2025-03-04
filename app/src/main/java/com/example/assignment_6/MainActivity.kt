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
        recyclerView.layoutManager =
            LinearLayoutManager(this) //used layout manager to put list items easily
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
        inner class ExpenseAdapter :       //using inner class for viewHolder to place individual items
            RecyclerView.Adapter<ExpenseAdapter.ExpenseView>() {
            inner class ExpenseView(itemView: View) :
                RecyclerView.ViewHolder(itemView) {
                val name: TextView = itemView.findViewById(R.id.expenseName)
                val amount: TextView = itemView.findViewById(R.id.expenseAmt)
                val btDelete: Button = itemView.findViewById(R.id.btDelete)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): //using built in recyclerview functions
                    ExpenseView {
                val view = LayoutInflater.from(parent.context)           //using layout inflate to attach item_expense.xml to main.xml
                    .inflate(R.layout.item_expense, parent, false)
                return ExpenseView(view)
            }

            override fun onBindViewHolder(holder: ExpenseView, position: Int) {     //using built in recyclerview functions
            val (name, amount) = listExpense[position]            //putting name and amount in listExpense list
            holder.name.text = name
            holder.amount.text = amount
            holder.btDelete.setOnClickListener {
                listExpense.removeAt(position)
                notifyItemRemoved(position)           //using recyclerview method notify
           }
       }
    override fun getItemCount(): Int = listExpense.size    //using getItemCount another RecyclerView adapter class function
    }
}

