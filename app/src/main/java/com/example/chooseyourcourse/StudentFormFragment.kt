package com.example.chooseyourcourse

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController

class StudentFormFragment : Fragment() {
    lateinit var databaseHelper : DatabaseHelper
    var studentId: Int = 0
    var showForm:Boolean = true
    var student: StudentModel? = null

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
        val editButton = view.findViewById<Button>(R.id.save)
        val cancelButton = view.findViewById<Button>(R.id.cancel)
        val navigateButton = view.findViewById<Button>(R.id.navigate)

        val controlStudent = view.findViewById<LinearLayout>(R.id.controlStudent)
        controlStudent.isVisible = true

        saveButton.setOnClickListener {
            if(!TextUtils.isEmpty(studentName.text.toString()) && !TextUtils.isEmpty(studentAge.text.toString()) ) {
                var student = StudentModel(name = studentName.text.toString(),
                    age = studentAge.text.toString().toInt(),)
                if (studentId > 0) {
                    StudentModel(name = studentName.text.toString(),
                        age = studentAge.text.toString().toInt(), id = studentId)
                    updateData(student)
                } else {
                    createData(student)
                }
            } else {
                Toast.makeText(activity, "Please fill up all of the fields",
                    Toast.LENGTH_LONG).show();
            }

        }

        cancelButton.setOnClickListener {
            studentName.text.clear()
            studentAge.text.clear()
        }

        editButton.setOnClickListener {
            val studentForm = view?.findViewById<LinearLayout>(R.id.studentForm)
            if (studentForm != null) {
                studentForm.isVisible = true
                getStudentData()
                if (student != null) {
                    studentName.setText(student!!.name)
                    studentAge.setText(student!!.age)
                }
            }
        }

        navigateButton.setOnClickListener {
            val action = StudentFormFragmentDirections.actionStudentFormFragmentToMarksFragment(studentId)
            view.findNavController().navigate(action)
        }

    }

    fun createData(studentModel: StudentModel) {
        try {
            val result = databaseHelper.createStudent(studentModel)
            if (result>0) {
                showForm = false
                val studentForm = view?.findViewById<LinearLayout>(R.id.studentForm)
                if (studentForm != null) {
                    studentForm.isVisible = showForm
                }
                studentId = result
                Toast.makeText(activity, "Student added successfully!",
                    Toast.LENGTH_LONG).show();
            }
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(activity, "Error while adding student!",
                Toast.LENGTH_LONG).show();
        }

    }

    fun updateData(studentModel: StudentModel) {
        try {
            val result = databaseHelper.updateStudent(studentModel)
            if (result) {
                showForm = false
                Toast.makeText(activity, "Student updated successfully!",
                    Toast.LENGTH_LONG).show();
            }
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(activity, "Error while updating student!",
                Toast.LENGTH_LONG).show();
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