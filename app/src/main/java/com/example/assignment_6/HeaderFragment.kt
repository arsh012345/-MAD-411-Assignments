package com.example.assignment_6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HeaderFragment : Fragment() {          //simple header fragment like footer if needed in future because header not
    override fun onCreateView(                   //adding without it
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_header, container, false)
    }
}
