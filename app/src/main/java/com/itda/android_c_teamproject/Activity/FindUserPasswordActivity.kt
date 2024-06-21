package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityFindUserPasswordBinding
import com.itda.android_c_teamproject.model.UserFindPasswordDTO
import com.itda.android_c_teamproject.model.UserFindPasswordResponse
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindUserPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindUserPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            buttonExit.setOnClickListener {
                startActivity(Intent(this@FindUserPasswordActivity, LoginActivity::class.java))
                finish()
            }

            buttonFindPassword.setOnClickListener {
                val username = editUsername.text.toString()
                val email = editEmail.text.toString()
                val phoneNumber = editPhoneNumber.text.toString()
                val dateOfBirth = editDateOfBirth.text.toString()

                if (username.isBlank() || email.isBlank() || phoneNumber.isBlank() || dateOfBirth.isBlank()) {
                    Toast.makeText(
                        this@FindUserPasswordActivity,
                        "모든 정보를 입력해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val userFindPasswordDTO = UserFindPasswordDTO(username, email, phoneNumber, dateOfBirth)

                RetrofitClient.api.findUserPassword(userFindPasswordDTO)
                    .enqueue(object : Callback<UserFindPasswordResponse> {
                        override fun onResponse(call: Call<UserFindPasswordResponse>, response: Response<UserFindPasswordResponse>) {
                            if (response.isSuccessful) {
                                val newPassword = response.body()?.newPassword
                                textFindPassword.text = newPassword
                                Toast.makeText(
                                    this@FindUserPasswordActivity,
                                    "임시 비밀번호 생성 성공 했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@FindUserPasswordActivity,
                                    "비밀번호 찾기에 실패했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<UserFindPasswordResponse>, t: Throwable) {
                            Toast.makeText(
                                this@FindUserPasswordActivity,
                                "네트워크 오류가 발생했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }
}