package com.example.chooseyourcourse

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHelper(val context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "chooseyourcourse.db"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        arrangeDatabase(db)
    }

    private fun arrangeDatabase(db: SQLiteDatabase?) {
        dropTables(db)
        createTables(db)
        insertSubjects(db)
        insertSchools(db)
    }

    private fun dropTables(db: SQLiteDatabase?) {
        db?.execSQL("Drop Table if exists Student")
        db?.execSQL("Drop Table if exists Subject")
        db?.execSQL("Drop Table if exists School")
        db?.execSQL("Drop Table if exists StudentSubject")
    }

    private fun createTables(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE Student(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Age TEXT, GPA DECIMAL(5,2));")
        db?.execSQL("CREATE TABLE Subject(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Code TEXT, Description TEXT, CreditHour DECIMAL(5,2));")
        db?.execSQL("CREATE TABLE School(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Description TEXT, UpperLimit DECIMAL(5,2), LowerLimit DECIMAL(5,2));")
        db?.execSQL("CREATE TABLE StudentSubject(Id INTEGER PRIMARY KEY AUTOINCREMENT, Mark Decimal(5,2), StudentId INTEGER, SubjectId Integer, FOREIGN KEY(SubjectId) REFERENCES Subject(Id), FOREIGN KEY(StudentId) REFERENCES Student(Id))")
    }

    private fun insertSubjects(db: SQLiteDatabase?) {
        db?.execSQL("""insert into Subject values 
            (1, "Mathematics", "math", "Mathematics is an area of knowledge that includes the topics of numbers, formulas and related structures, shapes and the spaces", 4),
             (2, "Science", "science", "Science is a continuing effort to discover and increase human knowledge and understanding through disciplined research", 5),
            (3, "Commerce", "commerce", "The Commerce program combines economics and the various sub-disciplines of management", 4),
            (4, "Language & Arts", "language_arts", "Language arts instruction typically consists of a combination of reading, writing (composition), speaking, and listening. ", 4),
            (5, "Humanities", "humanities", "Humanities courses can include the study of history, philosophy and religion, modern and ancient languages and literature, fine and performing arts, media and cultural studies", 3)
        """.trimMargin())
    }

    private fun insertSchools(db: SQLiteDatabase?) {
        db?.execSQL("""insert into School values 
            (1, "School of Engineering", "", 100, 90),
            (2, "School of Business", "", 90, 80),
            (3, "School of Arts", "", 80, 70),
            (4, "School of Hospitality & Management", "", 70, 60),
            (5, "Not Accepted", "", 60, 0)
        """.trimMargin())
    }

    fun listSchool(): ArrayList<SchoolModel> {
        val schoolList = ArrayList<SchoolModel>()
        val sqLiteDatabase = this.readableDatabase

        var queryString = "SELECT * FROM School"

        val cursor = try {
            sqLiteDatabase.rawQuery(queryString, null)
        }catch (e: Exception) {
            arrangeDatabase(sqLiteDatabase)
            sqLiteDatabase.rawQuery(queryString, null)
        }

        while (cursor.moveToNext()) {
            val model = SchoolModel(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getFloat(3),
                cursor.getFloat(4)
                )

            schoolList.add(model)
        }

        cursor.close()
        return schoolList
    }

    fun listSubject(): ArrayList<SubjectModel> {
        val subjectList = ArrayList<SubjectModel>()
        val sqLiteDatabase = this.readableDatabase

        var queryString = "SELECT * FROM Subject"

        val cursor = try {
            sqLiteDatabase.rawQuery(queryString, null)
        }catch (e: Exception) {
            arrangeDatabase(sqLiteDatabase)
            sqLiteDatabase.rawQuery(queryString, null)
        }

        while (cursor.moveToNext()) {
            val model = SubjectModel(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getFloat(4),
            )

            subjectList.add(model)
        }

        cursor.close()
        Log.i("info", subjectList.toString())
        return subjectList
    }

    fun createStudent(studentModel: StudentModel): Int {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name", studentModel.name)
        contentValues.put("Age", studentModel.age)
        contentValues.put("GPA", studentModel.gpa)
        val result = sqLiteDatabase.insert("Student", null, contentValues)
        println(result)

        Log.i("info", result.toString())
        if (result.toInt() == -1){
            return 0
        }
        return result.toInt()
    }

    fun updateStudent(studentModel: StudentModel): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name", studentModel.name)
        contentValues.put("Age", studentModel.age)
        contentValues.put("GPA", studentModel.gpa)
        val result = sqLiteDatabase.update("Student", contentValues, "Id="+studentModel.id, null)
        if (result.toInt() == -1){
            return false
        }
        return true
    }

    fun getStudentById(id: Int): StudentModel {
        val sqLiteDatabase = this.readableDatabase

        val query = "SELECT * FROM student where ID=$id"

        val cursor = try {
            sqLiteDatabase.rawQuery(query, null)
        }catch (e: java.lang.Exception) {
            arrangeDatabase(sqLiteDatabase)
            sqLiteDatabase.rawQuery(query, null)
        }
        cursor.moveToFirst()
        val student = StudentModel(cursor.getInt(0),
            cursor.getString(1),
            cursor.getInt(2),
            cursor.getDouble(3))
        cursor.close()

        return student
    }



//    fun createStudentSubject(studentSubjectModelList: ArrayList<StudentSubjectModel>): Boolean {
//        val sqLiteDatabase = this.writableDatabase
//        try {
//
//        }
//        val contentValues = ContentValues()
//        contentValues.put("Mark", studentSubjectModel.mark)
//        contentValues.put("SubjectId", studentSubjectModel.subjectId)
//        contentValues.put("StudentId", studentSubjectModel.studentId)
//        val result = sqLiteDatabase.insert("Student", null, contentValues)
//        if (result.toInt() == -1){
//            return false
//        }
//        return true
//    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


}