package com.itda.android_c_teamproject.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityUpdateUserPersonalBinding
import com.itda.android_c_teamproject.model.User
import com.itda.android_c_teamproject.model.UserPersonalDTO
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UpdateUserPersonalActivity"

class UpdateUserPersonalActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateUserPersonalBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var userPersonalDTO: UserPersonalDTO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)
        val token = getToken()

        binding.run {

            val username = sharedPreferences.getString("username", "") ?: ""

            buttonExit.setOnClickListener {
                startActivity(Intent(this@UpdateUserPersonalActivity, FirstActivity::class.java))
                finish()
            }

            RetrofitClient.api.getUserInfo("Bearer $token", username)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val user = response.body()
                        textUsername.text = "${username}님"
                        textEmail.text = "이메일 : ${user?.email.toString()}"
                        textPhoneNumber.text = "핸드폰 번호 : ${user?.phoneNumber.toString()}"
                        textDateOfBirth.text = "생년월일 : ${user?.dateOfBirth.toString()}"
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d(TAG, "onFailure: 네트워크 요청 실패")
                    }

                })


            buttonUpdate.setOnClickListener {

                val newPassword = editPassword.text.toString()
                val newEmail = editEmail.text.toString()
                val newPhoneNumber = editPhoneNumber.text.toString()
                val newDateOfBirth = editDateOfBirth.text.toString()

                userPersonalDTO = UserPersonalDTO(
                    newPassword, newEmail, newPhoneNumber, newDateOfBirth
                )


                RetrofitClient.api.updateUserPersonalInfo(
                    "Bearer $token", username, userPersonalDTO
                ).enqueue(object : Callback<UserPersonalDTO> {
                    override fun onResponse(
                        call: Call<UserPersonalDTO>, response: Response<UserPersonalDTO>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "onResponse: 정보 수정 성공 ${response.code()}")

                            val user = response.body()
                            textUsername.text = "${username}님"
                            textEmail.text = "이메일 : ${user?.email.toString()}"
                            textPhoneNumber.text = "핸드폰 번호 : ${user?.phoneNumber.toString()}"
                            textDateOfBirth.text = "생년월일 : ${user?.dateOfBirth.toString()}"
                            Toast.makeText(
                                this@UpdateUserPersonalActivity,
                                "정보 수정 완료 했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            Log.d(TAG, "onResponse: 정보 수정 실패 ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<UserPersonalDTO>, t: Throwable) {
                        Log.d(TAG, "onFailure: 네트워크 요청 실패")
                    }
                })
            }


        }

    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)
        return sharedPreferences.getString("token", null) ?: ""
    }
}