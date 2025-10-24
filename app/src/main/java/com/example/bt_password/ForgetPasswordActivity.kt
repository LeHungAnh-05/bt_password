package com.example.bt_password

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ForgetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val tvError = findViewById<TextView>(R.id.tvError)

        btnNext.setOnClickListener {
            val email = edtEmail.text.toString().trim()

            when {
                email.isEmpty() -> {
                    tvError.text = "Email không hợp lệ"
                    tvError.visibility = TextView.VISIBLE
                }
                !email.contains("@") -> {
                    tvError.text = "Email không đúng định dạng"
                    tvError.visibility = TextView.VISIBLE
                }
                else -> {
                    tvError.text = "Email hợp lệ"
                    tvError.setTextColor(getColor(android.R.color.holo_green_dark))
                    tvError.visibility = TextView.VISIBLE

                    val intent = Intent(this, VerifyCodeActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
            }
        }
    }
}
