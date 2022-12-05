package com.example.chooseyourcourse

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import org.w3c.dom.Text

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
        val editButton = view.findViewById<Button>(R.id.edit)
        val cancelButton = view.findViewById<Button>(R.id.cancel)
        val navigateButton = view.findViewById<Button>(R.id.navigate)
        val deleteButton = view.findViewById<Button>(R.id.delete)

        val studentNameLabel = view.findViewById<TextView>(R.id.studentNameLabel)
        val studentAgeLabel = view.findViewById<TextView>(R.id.studentAgeLabel)

        setVisibilityControlStudent(false)

        saveButton.setOnClickListener {
            if(!TextUtils.isEmpty(studentName.text.toString()) && !TextUtils.isEmpty(studentAge.text.toString()) ) {
                var student = StudentModel(name = studentName.text.toString(),
                    age = studentAge.text.toString().toInt(),)
                if (studentId > 0) {
                    StudentModel(name = studentName.text.toString(),
                        age = studentAge.text.toString().toInt(), id = studentId)
                    updateData(student)
                    getStudentData()
                    Log.i("Info", student.toString())
                    studentNameLabel.setText(student.name)
                    studentAgeLabel.setText(student.age.toString())
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
                setVisibilityControlStudent(false)
                getStudentData()
                if (student != null) {
                    studentName.setText(student!!.name)
                    studentAge.setText(student!!.age.toString())
                }
            }
        }

        navigateButton.setOnClickListener {
            val action = StudentFormFragmentDirections.actionStudentFormFragmentToMarksFragment(studentId)
            view.findNavController().navigate(action)
        }

        deleteButton.setOnClickListener {
            if(studentId>0) {
                deleteData(studentId)
                setVisibilityControlStudent(false)
                setVisibiltyForm(true)
                resetForm()
            }
        }

    }

    fun createData(studentModel: StudentModel) {
        try {
            val result = databaseHelper.createStudent(studentModel)
            if (result>0) {
                showForm = false
                val studentForm = view?.findViewById<LinearLayout>(R.id.studentForm)
                if (studentForm != null) {
                    studentForm.isVisible = false
                    val studentName = view?.findViewById<TextView>(R.id.studentNameLabel)
                    val studentAge = view?.findViewById<TextView>(R.id.studentAgeLabel)
                    setVisibilityControlStudent(true)
                    studentName?.setText(studentModel.name)
                    studentAge?.setText(studentModel.age.toString())
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
                setVisibilityControlStudent(true)
                setVisibiltyForm(false)
                Toast.makeText(activity, "Student updated successfully!",
                    Toast.LENGTH_LONG).show();
            }
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(activity, "Error while updating student!",
                Toast.LENGTH_LONG).show();
        }

    }

     fun setVisibilityControlStudent(flag: Boolean) {
         val controlStudent = view?.findViewById<LinearLayout>(R.id.controlStudent)
         controlStudent?.isVisible = flag
    }

    fun setVisibiltyForm(flag: Boolean) {
        val studentForm = view?.findViewById<LinearLayout>(R.id.studentForm)
        studentForm?.isVisible = flag
    }


    fun deleteData(id: Int) {
        try {
            val result = databaseHelper.deleteStudent(id)
            if (result) {
                showForm = true
                Toast.makeText(activity, "Student deleted successfully!",
                    Toast.LENGTH_LONG).show();
            }
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(activity, "Error while deleting student!",
                Toast.LENGTH_LONG).show();
        }

    }

    fun resetForm() {
        val studentName = view?.findViewById<EditText>(R.id.studentName)
        val studentAge = view?.findViewById<EditText>(R.id.studentAge)
        studentName?.text?.clear()
        studentAge?.text?.clear()
    }

    fun getStudentData() {
        try {
            student = databaseHelper.getStudentById(studentId.toInt())
            Log.i("Info", student.toString())
        } catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(activity, "Error while adding student!",
                Toast.LENGTH_LONG).show();
        }
    }
}