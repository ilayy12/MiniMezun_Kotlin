package com.example.minimezun

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    public override fun onStart() {
        super.onStart()
        // Kullanıcı zaten uygulamaya giriş yapmışsa, ekran MainActivitye geçer
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intentGoToMain = Intent(this,MainActivity::class.java)
            startActivity(intentGoToMain)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        // xml tanımlamalarının çekilmesi
        val loginButton = findViewById<Button>(R.id.login_btn)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val registerNowText = findViewById<TextView>(R.id.registerNow)
        //register ekranına yönlendiren buton
        registerNowText.setOnClickListener {
            val intentRegisterNow = Intent(this,RegisterActivity::class.java)
            startActivity(intentRegisterNow)
            finish()
        }
        loginButton.setOnClickListener {
            val emailText = emailEditText.text
            val passwordText = passwordEditText.text

            if(emailText.isEmpty()){
                Toast.makeText(this, "Lütfen Emailinizi Giriniz", Toast.LENGTH_SHORT).show()
            }
            if(passwordText.isEmpty()){
                Toast.makeText(this, "Lütfen Şifrenizi Giriniz", Toast.LENGTH_SHORT).show()
            }

            auth.signInWithEmailAndPassword(emailText.toString(), passwordText.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intentGoToMain = Intent(this,MainActivity::class.java)
                        startActivity(intentGoToMain)
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Giriş Başarısız!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}