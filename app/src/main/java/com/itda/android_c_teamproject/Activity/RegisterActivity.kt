package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityRegisterBinding
import com.itda.android_c_teamproject.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            if (buttonMale.isClickable) {
                buttonMale.setOnClickListener {
                    textGender.text = "남"
                }
            }
            if (buttonFemale.isClickable) {
                buttonFemale.setOnClickListener {
                    textGender.text = "여"
                }
            }
            buttonRegister.setOnClickListener {
                val username = editId.text.toString()
                val password = editPassword.text.toString()
                val email = editEmail.text.toString()
                val phoneNumber = editPhoneNumber.text.toString()
                val dateOfBirth = editDateOfBirth.text.toString()
                val weight = editWeight.text.toString().toInt()
                val height = editHeight.text.toString().toDouble()
                val gender = textGender.text.toString()
                val user = User(
                    username,
                    password,
                    email,
                    phoneNumber,
                    dateOfBirth,
                    gender,
                    weight,
                    height
                )

                RetrofitClient.api.createUser(user).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "화원가입에 성공하셨습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("mylog", "onResponse: ${response.body()}")
                            // 회원 가입 성공시 메시지 띄우고, 로그인 화면으로 이동
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        } else {
                            // 실패
                            Toast.makeText(
                                this@RegisterActivity,
                                "화원가입에 실패하셨습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("mylog", "onResponse, 회원가입실패: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, "회원가입 네트워크 요청 실패", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("mylog", "onFailure: ${t.message}")
                    }
                })
            }
        }
    }
}