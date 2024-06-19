package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityFindUserPasswordBinding
import com.itda.android_c_teamproject.model.UserFindPasswordDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFindPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindUserPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            buttonExit.setOnClickListener {
                startActivity(Intent(this@UserFindPasswordActivity, LoginActivity::class.java))
                finish()
            }

            buttonFindPassword.setOnClickListener {
                val username = editUsername.text.toString()
                val email = editEmail.text.toString()
                val phoneNumber = editPhoneNumber.text.toString()
                val dateOfBirth = editDateOfBirth.text.toString()

                val userFindPasswordDTO = UserFindPasswordDTO(username, email, phoneNumber, dateOfBirth)

                RetrofitClient.api.userFindPassword(userFindPasswordDTO)
                    .enqueue(object : Callback<UserFindPasswordDTO> {
                        override fun onResponse(call: Call<UserFindPasswordDTO>, response: Response<UserFindPasswordDTO>) {
                            if (response.isSuccessful) {
                                val newPassword = response.body().toString()
                                textFindPassword.text = newPassword
                                Toast.makeText(
                                    this@UserFindPasswordActivity,
                                    "임시 비밀번호 생성 성공 했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@UserFindPasswordActivity,
                                    "비밀번호 찾기에 실패했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<UserFindPasswordDTO>, t: Throwable) {
                            Toast.makeText(
                                this@UserFindPasswordActivity,
                                "네트워크 오류가 발생했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }
}