package com.example.assignment_6

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.addTextChangedListener
import com.example.assignment_6.network.RetrofitInterface
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
    private lateinit var currencySpinner: Spinner
    private lateinit var conversionNeeded: SwitchMaterial     //google material design switch added to enable currency spinner
   // private lateinit var totalTextView: TextView
    private lateinit var convertView: TextView
    private lateinit var expenseAdapter: ExpenseAdapter
    private var listExpense = mutableListOf<Expense>()          //making mutable list
    private var cadRates: Map<String, Double> = emptyMap()
    private var selectedCurrency: String = "CAD"            //putting default string to CAD

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_expense_list, container, false)

        Log.d("Lifecycle", "onCreate called")      //Logging lifecycle method for create

        mainExpenseName = view.findViewById(R.id.mainExpenseName)
        mainExpenseAmt = view.findViewById(R.id.mainExpenseAmt)
        mainExpenseDate = view.findViewById(R.id.mainExpenseDate)
        btAdd = view.findViewById(R.id.btAdd)
        recyclerView = view.findViewById(R.id.recyclerView)           //putting every require id's here
        currencySpinner = view.findViewById(R.id.mainSpinner)
        conversionNeeded = view.findViewById(R.id.switchOn)                //conversion button declared here
        convertView = view.findViewById(R.id.convertView)
        btFinancialTips = view.findViewById(R.id.btFinancialTips)
        //totalTextView = view.findViewById(R.id.totalExpenses)

        mainExpenseAmt.addTextChangedListener{
            updateConvert()       //calling update convert here
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        expenseAdapter = ExpenseAdapter()
        recyclerView.adapter = expenseAdapter

        listExpense = loadExpenses()
        expenseAdapter.notifyDataSetChanged()
        updateFooterTotal()
        fetchRates()

        btAdd.setOnClickListener {
            val name = mainExpenseName.text.toString().trim()
            val amount = mainExpenseAmt.text.toString().trim()
            val date = mainExpenseDate.text.toString().trim()           //added new date here

            if (name.isNotEmpty() && amount.isNotEmpty() && date.isNotEmpty()){
                val rate = cadRates[selectedCurrency] ?: 1.0
                val amountDouble = amount.toDoubleOrNull() ?: 0.0
                val converted = if (conversionNeeded.isChecked) amountDouble / rate else 0.0
                //amount divided by rate if switch is checked

                if(conversionNeeded.isChecked){       //simple logic for textview of converted cost
                    convertView.text = "Converted:- %.2f CAD".format(converted)
                    convertView.visibility = View.VISIBLE
                }                //converted amount view if converstion is done
                else{
                    convertView.visibility = View.GONE
                }

                val expense = Expense(
                    name = name,
                    amount = amount,
                    date = date,
                    currency = selectedCurrency,
                    convertedCost = converted
                )
                listExpense.add(expense)
                expenseAdapter.notifyItemInserted(listExpense.size - 1)
                updateFooterTotal()
                saveExpenses()
                mainExpenseName.text.clear()
                mainExpenseAmt.text.clear()
                mainExpenseDate.text.clear()
            }
            else
            {
                Snackbar.make(view, "Please enter in all fields", Snackbar.LENGTH_SHORT).show()
            }       //using snackbar instead of toast
        }

        btFinancialTips.setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.nerdwallet.com"))
            startActivity(intent) //intent.action_view to open webpage
        }

        conversionNeeded.setOnCheckedChangeListener { _, isChecked ->            //listener add for switch to enable spinner to display first item
            currencySpinner.isEnabled = isChecked
            if(isChecked){
                currencySpinner.setSelection(0)
            }
        }

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.headerContainer, HeaderFragment())
        transaction.replace(R.id.footerContainer, FooterFragment()) //added fragment of header and footer here
        transaction.commit()                      //transactions for header and footer
        recyclerView.post {
            updateFooterTotal() //updating expense list everytime app opens
        }
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

    private fun fetchRates(){
        lifecycleScope.launch{                           //lifecycle start here
            try{
                val response = withContext(Dispatchers.IO){
                    RetrofitInterface.api.getRates()
                }

                val allRates = response.conversion_rates
                val usdToCad = allRates["CAD"] ?: throw IllegalStateException("CAD not avalible")
                cadRates = allRates.mapValues { (_, usdToCurrency) -> usdToCurrency / usdToCad }

                val currencyList = cadRates.keys.sorted()     //cadRates list sorted
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencyList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                currencySpinner.adapter = adapter    //adapter with built in spinner ids

                currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
                {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long)
                    {
                        selectedCurrency = currencyList[position]
                    }
                    override fun onNothingSelected(parent: AdapterView<*>){} //nothing selected stay empty
                }
            }
            catch(e: Exception)
            {
                Toast.makeText(requireContext(), "Failed in fetching", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateConvert(){             //update textbox after amount entered
        val amountText = mainExpenseAmt.text.toString().trim()
        val amountDouble = amountText.toDoubleOrNull()
        val rate = cadRates[selectedCurrency] ?: return

        if(conversionNeeded.isChecked && amountDouble != null){
            val converted = amountDouble / rate
            convertView.text = "Converted:- %.2f CAD".format(converted)
            convertView.visibility = View.VISIBLE
        }
        else{
            convertView.visibility = View.GONE
        }
    }

    private fun updateFooterTotal() {
        val total = listExpense.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        val footerFragment = parentFragmentManager.findFragmentById(R.id.footerContainer) as? FooterFragment
        footerFragment?.updateTotal("Total Expenses: $$total")
    }                           //updating footer total here

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
        }
        catch (e: FileNotFoundException)
        {
            Log.e("FileStorage", "File not found: ${e.message}")
        } catch (e: IOException)
        {
            Log.e("FileStorage", "Error reading file: ${e.message}")
        }
        return expenseList
    }

    inner class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseView>(){
        inner class ExpenseView(itemView: View) : RecyclerView.ViewHolder(itemView){
            val name: TextView = itemView.findViewById(R.id.expenseName)
            val amount: TextView = itemView.findViewById(R.id.expenseAmt)
            val date: TextView = itemView.findViewById(R.id.expenseDate)
            val converted: TextView = itemView.findViewById(R.id.convertedCost) //added coverted cost id here
            val btDelete: Button = itemView.findViewById(R.id.btDelete)
            val btDetails: Button = itemView.findViewById(R.id.btDetails) //details added here
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseView {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
            return ExpenseView(view)
        }

        override fun onBindViewHolder(holder: ExpenseView, position: Int){
            val expense = listExpense[position]
            holder.name.text = expense.name
            holder.amount.text = "${expense.amount} ${expense.currency}"  //it will for original currency
            holder.date.text = expense.date         //added date in binding
            holder.converted.text = "CAD:- ${"%.2f".format(expense.convertedCost)}"  //it will show the converted cost

            holder.btDelete.setOnClickListener {
                listExpense.removeAt(position)
                notifyItemRemoved(position)
                updateFooterTotal()           //Updating total after delete
                saveExpenses()
                Snackbar.make(holder.itemView, "Expense has been deleted", Snackbar.LENGTH_SHORT).show()
            }

            holder.btDetails.setOnClickListener {
                val action = ExpenseListFragmentDirections
                    .actionExpenseListFragmentToExpenseDetailsFragment(
                        name = expense.name,
                        amount = expense.amount,   //taking name,amount,date,currency and convertedcost with action
                        date = expense.date,
                        currency = expense.currency,
                        convertedCost = expense.convertedCost.toFloat()   //converted cost in float
                    )
                findNavController().navigate(action)
            }
        }
        override fun getItemCount(): Int = listExpense.size
    }
}