package com.example.chooseyourcourse

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MarksFragment : Fragment() {
    lateinit var databaseHelper : DatabaseHelper
    var studentId: Int = 0
    var showForm:Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val id = MarksFragmentArgs.fromBundle(requireArguments()).id
        Toast.makeText(activity, id.toString(),
            Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_marks, container, false)
        databaseHelper = DatabaseHelper(view.context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mathScore = view.findViewById<EditText>(R.id.math)
        val commerceScore = view.findViewById<EditText>(R.id.commerce)
        val scienceScore = view.findViewById<EditText>(R.id.science)
        val languageScore = view.findViewById<EditText>(R.id.language)
        val humanitiesScore = view.findViewById<EditText>(R.id.humanities)
        val gpa = view.findViewById<TextView>(R.id.gpa)
        val school = view.findViewById<TextView>(R.id.school)

        val calculate = view.findViewById<Button>(R.id.calculate)
        calculate.setOnClickListener {
            if(!TextUtils.isEmpty(mathScore.text.toString()) && !TextUtils.isEmpty(commerceScore.text.toString()) &&
                !TextUtils.isEmpty(scienceScore.text.toString()) && !TextUtils.isEmpty(languageScore.text.toString()) &&
                        !TextUtils.isEmpty(humanitiesScore.text.toString()) ){

                // code to calculate gpa

                // after gpa has been calculated
                gpa.text = "4"

                // code to assign schools
                var schoolList = getSchools()
                school.text = schoolList.toString()
            } else {
                Toast.makeText(activity, "Please fill up all of the fields",
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    fun getSchools(): ArrayList<SchoolModel> {
         return databaseHelper.listSchool()
    }

}