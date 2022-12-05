package com.example.chooseyourcourse

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserLogInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_log_in)

        //Initialization Firebase
        auth = FirebaseAuth.getInstance()

        val logInBtn = findViewById<Button>(R.id.logInBtn)
        logInBtn.setOnClickListener {
            FirebaseLogIn()
        }
    }

    fun FirebaseLogIn(){
        val emailId = findViewById<EditText>(R.id.emailidEditTxt)
        val password = findViewById<EditText>(R.id.passwordEditTxt)
        auth.signInWithEmailAndPassword(emailId.text.toString(),password.text.toString()).addOnCompleteListener(this) { task->
            if(task.isSuccessful){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Log.d(TAG,"User can't log In!!!")
            }
        }
    }
}