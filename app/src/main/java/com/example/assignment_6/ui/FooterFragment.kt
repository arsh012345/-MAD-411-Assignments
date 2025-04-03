package com.example.assignment_6.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.assignment_6.R

class FooterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_footer, container, false)
    }

    fun updateTotal(totalHere: String) {              //update total fun in footer
        view?.findViewById<TextView>(R.id.totalExpenses)?.text = totalHere
    }
}