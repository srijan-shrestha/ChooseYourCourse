package com.example.chooseyourcourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class StudentFormFragment : Fragment() {
    lateinit var databaseHelper : DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_student_form, container, false)
        databaseHelper = DatabaseHelper(view.context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentName = view.findViewById<EditText>(R.id.studentName)
        val studentAge = view.findViewById<EditText>(R.id.studentAge)
        val saveButton = view.findViewById<Button>(R.id.save)
        var studentObj: StudentModel
//        studentObj.age = 12
        saveButton.setOnClickListener {
//            databaseHelper.createStudent()
        }

    }

}