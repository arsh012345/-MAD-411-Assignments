package com.example.assignment_6

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExpenseListFragment : Fragment() {
    private lateinit var mainExpenseName: EditText
    private lateinit var mainExpenseAmt: EditText
    private lateinit var mainExpenseDate: EditText
    private lateinit var btAdd: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var btFinancialTips: Button                 //button for financial tip
    private lateinit var totalTextView: TextView

    private lateinit var expenseAdapter: ExpenseAdapter
    private var listExpense = mutableListOf<Expense>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_expense_list, container, false)
        Log.d("Lifecycle", "onCreate called")      //Logging lifecycle method for create

        mainExpenseName = view.findViewById(R.id.mainExpenseName)
        mainExpenseAmt = view.findViewById(R.id.mainExpenseAmt)
        mainExpenseDate = view.findViewById(R.id.mainExpenseDate)
        btAdd = view.findViewById(R.id.btAdd)
        recyclerView = view.findViewById(R.id.recyclerView)           //putting every require id's here
        btFinancialTips = view.findViewById(R.id.btFinancialTips)
        totalTextView = view.findViewById(R.id.totalExpenses)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        expenseAdapter = ExpenseAdapter()
        recyclerView.adapter = expenseAdapter

        loadExpenses()
        updateFooterTotal()

        btAdd.setOnClickListener {
            val name = mainExpenseName.text.toString().trim()
            val amount = mainExpenseAmt.text.toString().trim()
            val date = mainExpenseDate.text.toString().trim()           //added new date here

            if (name.isNotEmpty() && amount.isNotEmpty() && date.isNotEmpty()) {
                val expense = Expense(name, amount, date)
                listExpense.add(expense)
                expenseAdapter.notifyItemInserted(listExpense.size - 1)
                updateFooterTotal()
                saveExpenses()
                mainExpenseName.text.clear()
                mainExpenseAmt.text.clear()
                mainExpenseDate.text.clear()
            } else {
                Snackbar.make(view, "Please enter in all fields", Snackbar.LENGTH_SHORT).show()
            } //using snackbar instead of toast
        }

        btFinancialTips.setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.nerdwallet.com"))
            startActivity(intent) //intent.action_view to open webpage
        }

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.headerContainer, HeaderFragment())
        transaction.replace(R.id.footerContainer, FooterFragment()) //added fragment of header and footer here
        transaction.commit()

        return view
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
        val total = listExpense.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        totalTextView.text = "Total Expenses: $$total"            //updating footer total here
    }

    private fun saveExpenses() {                               //saving expense in json file here
        val jsonStr = Gson().toJson(listExpense)
        requireContext().openFileOutput("data_expense.json", android.content.Context.MODE_PRIVATE).use {
            it.write(jsonStr.toByteArray())
        }
    }

    private fun loadExpenses(): MutableList<Expense> {
        val expenseList: MutableList<Expense> = mutableListOf()       //loading from file here
        try {
            val file = File(requireContext().filesDir, "data_expense.json")
            if (!file.exists()) return expenseList

            val json = file.readText()
            val type = object : TypeToken<List<Expense>>() {}.type
            val loadedExpenses: List<Expense> = Gson().fromJson(json, type)
            expenseList.addAll(loadedExpenses)

            Log.d("FileStorage", "Expenses loaded successfully")
        } catch (e: FileNotFoundException) {
            Log.e("FileStorage", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error reading file: ${e.message}")
        }
        return expenseList
    }


    inner class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseView>() {
        inner class ExpenseView(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.expenseName)
            val amount: TextView = itemView.findViewById(R.id.expenseAmt)
            val date: TextView = itemView.findViewById(R.id.expenseDate)
            val btDelete: Button = itemView.findViewById(R.id.btDelete)
            val btDetails: Button = itemView.findViewById(R.id.btDetails) //details added here
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseView {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
            return ExpenseView(view)
        }

        override fun onBindViewHolder(holder: ExpenseView, position: Int) {
            val expense = listExpense[position]
            holder.name.text = expense.name
            holder.amount.text = expense.amount
            holder.date.text = expense.date         //added date in binding

            holder.btDelete.setOnClickListener {
                listExpense.removeAt(position)
                notifyItemRemoved(position)
                updateFooterTotal()           //Updating total after delete
                saveExpenses()
                Snackbar.make(holder.itemView, "Expense Deleted", Snackbar.LENGTH_SHORT).show()
            }

            holder.btDetails.setOnClickListener {
                val action = ExpenseListFragmentDirections
                    .actionExpenseListFragmentToExpenseDetailsFragment(
                        name = expense.name,
                        amount = expense.amount,   //taking name,amount and date
                        date = expense.date
                    )
                findNavController().navigate(action)
            }
        }
        override fun getItemCount(): Int = listExpense.size
    }
}
