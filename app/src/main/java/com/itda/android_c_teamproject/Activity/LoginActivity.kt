package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityLoginBinding
import com.itda.android_c_teamproject.model.LoginRequest
import com.itda.android_c_teamproject.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

                RetrofitClient.api.login(user).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }

            textRegister.setOnClickListener{
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

    }
}