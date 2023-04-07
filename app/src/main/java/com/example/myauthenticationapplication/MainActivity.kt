package com.example.myauthenticationapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var textFullName: TextView
    lateinit var textEmail: TextView
    lateinit var btnLogout: Button

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textFullName = findViewById(R.id.full_name)
        textEmail = findViewById(R.id.email)
        btnLogout = findViewById(R.id.btn_logout)

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser!=null){
            textFullName.text = firebaseUser.displayName
            textEmail.text = firebaseUser.email
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}