package com.example.assignment_6

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var mainExpenseName: EditText
    private lateinit var mainExpenseAmt: EditText
    private lateinit var mainExpenseDate: EditText
    private lateinit var btAdd: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var btFinancialTips: Button                      //button for financial tip
    private lateinit var footerFragment: FooterFragment                 //footerFragment
    private val listExpense = mutableListOf<Triple<String, String, String>>() //created name amount and date in list
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Lifecycle", "onCreate called")      //Logging lifecycle method for create

        mainExpenseName = findViewById(R.id.mainExpenseName)
        mainExpenseAmt = findViewById(R.id.mainExpenseAmt)            //putting every require id's here
        mainExpenseDate = findViewById(R.id.mainExpenseDate)
        btAdd = findViewById(R.id.btAdd)
        recyclerView = findViewById(R.id.recyclerView)
        btFinancialTips = findViewById(R.id.btFinancialTips)

        recyclerView.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseAdapter()
        recyclerView.adapter = expenseAdapter

        footerFragment = FooterFragment()        //created FooterFragment
        addFragments()                           //Loading it

        btAdd.setOnClickListener {
            val name = mainExpenseName.text.toString().trim()
            val amount = mainExpenseAmt.text.toString().trim()
            val date = mainExpenseDate.text.toString().trim()          //added new date here

            if (name.isNotEmpty() && amount.isNotEmpty() && date.isNotEmpty()) {
                listExpense.add(Triple(name, amount, date))
                expenseAdapter.notifyItemInserted(listExpense.size - 1)
                updateFooterTotal() // Update expense total
                mainExpenseName.text.clear()
                mainExpenseAmt.text.clear()
                mainExpenseDate.text.clear()
            } else {
                Snackbar.make(it, "Please enter all fields", Snackbar.LENGTH_SHORT).show()
                //using snackbar instead of toast
            }
        }

        btFinancialTips.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nerdwallet.com"))
            //intent.action_view to open webpage
            startActivity(intent)
        }
    }

    //logs added here
    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "onDestroy called")
    }

    private fun updateFooterTotal() {
        val total = listExpense.sumOf { it.second.toDoubleOrNull() ?: 0.0 }
        footerFragment.updateTotalExpense(total)                //updating footer total here
    }

    private fun addFragments() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        transaction.add(R.id.headerContainer, HeaderFragment())      //added fragment of header and footer here
        transaction.add(R.id.footerContainer, footerFragment)
        transaction.commit()
    }

    inner class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseView>() {
        inner class ExpenseView(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.expenseName)
            val amount: TextView = itemView.findViewById(R.id.expenseAmt)
            val btDelete: Button = itemView.findViewById(R.id.btDelete)
            val btDetails: Button = itemView.findViewById(R.id.btDetails)      //details added here
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseView {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_expense, parent, false)
            return ExpenseView(view)
        }

        override fun onBindViewHolder(holder: ExpenseView, position: Int) {
            val (name, amount, date) = listExpense[position]           //added date in binding
            holder.name.text = name
            holder.amount.text = amount

            holder.btDelete.setOnClickListener {
                listExpense.removeAt(position)
                notifyItemRemoved(position)
                Snackbar.make(it, "Expense Deleted", Snackbar.LENGTH_LONG).show()
                updateFooterTotal()                   //Updating total after delete
            }

        }
        override fun getItemCount(): Int = listExpense.size
    }
}
