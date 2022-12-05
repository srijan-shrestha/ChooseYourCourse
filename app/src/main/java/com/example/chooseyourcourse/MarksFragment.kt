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
    var student: StudentModel? = null
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
        if (studentId.toInt()>0) {
            getStudentData()
        }
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

                val marks = ArrayList<Float>()
                marks.add(mathScore.text.toString().toFloat())
                marks.add(commerceScore.text.toString().toFloat())
                marks.add(scienceScore.text.toString().toFloat())
                marks.add(languageScore.text.toString().toFloat())
                marks.add(humanitiesScore.text.toString().toFloat())

                // code to calculate gpa
                val totalGPA = calculateGPA(marks)
                gpa.text = totalGPA.toString()
                school.text = assignSchool(totalGPA)



                // code to assign schools
                var schoolList = getSchools()
//                school.text = schoolList.toString()
            } else {
                Toast.makeText(activity, "Please fill up all of the fields",
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    fun getSchools(): ArrayList<SchoolModel> {
         return databaseHelper.listSchool()
    }

    fun calculateGPA(marks:ArrayList<Float>): Float{
        var totalGPA = 0.0f
        for(s in marks){
            totalGPA += ((s*4.0f)/20.0f)
        }

        return totalGPA
    }

    fun assignSchool(gpa:Float): String{
        if(gpa in 90.0..100.0){
            return "School Of Engineering, School Of Business and Law School"
        }
        else if (gpa in 80.0..90.0){
            return "School Of Business and Law School"
        }
        else if (gpa in 70.0..80.0){
            return "Law School"
        }
        else{
            return "Not Accepted, since gpa"
        }
    }
    fun getStudentData() {
        try {
            student = databaseHelper.getStudentById(studentId.toInt())
        } catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(activity, "Error while adding student!",
                Toast.LENGTH_LONG).show();
        }
    }

    fun saveStudentGPA(gpa: Float) {
        try {
            student?.gpa  = gpa.toDouble()
            val result = student?.let { databaseHelper.updateStudent(it) }
            if (result == true) {
                Toast.makeText(activity, "GPA updated successfully!",
                    Toast.LENGTH_LONG).show();
            }
        } catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(activity, "Error while adding student!",
                Toast.LENGTH_LONG).show();
        }
    }


}