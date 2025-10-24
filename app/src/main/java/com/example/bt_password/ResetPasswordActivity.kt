package com.example.bt_password

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val email = intent.getStringExtra("email")
        val code = intent.getStringExtra("code")

        val edtPassword = findViewById<EditText>(R.id.edtPass)
        val edtConfirm = findViewById<EditText>(R.id.edtConfirmPass)
        val tvError = findViewById<TextView>(R.id.tvPasswordError)
        val btnNext = findViewById<Button>(R.id.btnNextReset)

        btnNext.setOnClickListener {
            val pass = edtPassword.text.toString()
            val confirm = edtConfirm.text.toString()

            when {
                pass.isEmpty() || confirm.isEmpty() -> {
                    tvError.text = "Vui lòng nhập đầy đủ thông tin"
                    tvError.setTextColor(getColor(android.R.color.holo_red_dark))
                    tvError.visibility = TextView.VISIBLE
                }
                pass != confirm -> {
                    tvError.text = "Mật khẩu không khớp"
                    tvError.setTextColor(getColor(android.R.color.holo_red_dark))
                    tvError.visibility = TextView.VISIBLE
                }
                else -> {
                    tvError.text = "Mật khẩu hợp lệ"
                    tvError.setTextColor(getColor(android.R.color.holo_green_dark))
                    tvError.visibility = TextView.VISIBLE

                    val intent = Intent(this, ConfirmActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("code", code)
                    intent.putExtra("password", pass)
                    startActivity(intent)
                }
            }
        }
    }
}
