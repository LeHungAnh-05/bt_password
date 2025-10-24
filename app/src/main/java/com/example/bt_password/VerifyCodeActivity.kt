package com.example.bt_password

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class VerifyCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code)

        val email = intent.getStringExtra("email")
        val tvDesc = findViewById<TextView>(R.id.tvEmailDesc)
        val tvError = findViewById<TextView>(R.id.tvError)
        val tvResend = findViewById<TextView>(R.id.tvResend)
        val btnContinue = findViewById<Button>(R.id.btnContinue)

        val otpFields = listOf<EditText>(
            findViewById(R.id.otp1),
            findViewById(R.id.otp2),
            findViewById(R.id.otp3),
            findViewById(R.id.otp4),
            findViewById(R.id.otp5)
        )

        tvDesc.text = "We’ve sent the code to $email"


        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < otpFields.size - 1) {
                        otpFields[i + 1].requestFocus()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        btnContinue.setOnClickListener {
            val code = otpFields.joinToString("") { it.text.toString() }

            if (code.length < 5) {
                tvError.text = "Vui lòng nhập đầy đủ mã xác nhận"
                tvError.visibility = TextView.VISIBLE
            } else {
                tvError.visibility = TextView.GONE
                Toast.makeText(this, "Xác nhận thành công", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ResetPasswordActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("code", code)
                startActivity(intent)
            }
        }

        tvResend.setOnClickListener {
            Toast.makeText(this, "Code resent to $email", Toast.LENGTH_SHORT).show()
        }
    }
}
