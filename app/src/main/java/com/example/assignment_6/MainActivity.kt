package com.example.assignment_6

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var viewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.edit_text)
        viewResult = findViewById(R.id.view_name)
        val buttonShow = findViewById<Button>(R.id.show_button)

        buttonShow.setOnClickListener {
            showResult(it)
        }
    }

    fun showResult(view: View) {
        val name = editText.text.toString().trim()
        viewResult.text = if (name.isNotEmpty()) "Hello, $name!" else "Enter your name in above box."
    }
}