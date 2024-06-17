package com.itda.android_c_teamproject.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityLoginBinding
import com.itda.android_c_teamproject.model.LoginRequest
import com.itda.android_c_teamproject.model.User

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            buttonLogin.setOnClickListener {
                val username = editID.text.toString()
                val password = editPassword.text.toString()
                val user = LoginRequest(username, password)

                RetrofitClient.api.login(user)
            }
        }

    }
}