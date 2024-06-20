package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.network.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityFindUserNameBinding
import com.itda.android_c_teamproject.model.UserFindNameDTO
import com.itda.android_c_teamproject.model.UserFindNameResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserFindNameActivity"

class UserFindNameActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindUserNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            buttonExit.setOnClickListener {
                startActivity(Intent(this@UserFindNameActivity, LoginActivity::class.java))
            }

            buttonFindUserName.setOnClickListener {
                val email = editEmail.text.toString()
                val phoneNumber = editPhoneNumber.text.toString()
                val dateOfBirth = editDateOfBirth.text.toString()

                if (email.isEmpty() || phoneNumber.isEmpty() || dateOfBirth.isEmpty()) {
                    Toast.makeText(
                        this@UserFindNameActivity,
                        "모든 정보를 입력해주세요",
                        Toast.LENGTH_SHORT
                        ).show()
                }

                val userFindNameDTO = UserFindNameDTO(email, phoneNumber, dateOfBirth)

                RetrofitClient.api.userFindName(userFindNameDTO).enqueue(object :
                    Callback<UserFindNameResponse> {
                    override fun onResponse(
                        call: Call<UserFindNameResponse>,
                        response: Response<UserFindNameResponse>
                    ) {
                        if (response.isSuccessful) {
                            val username = response.body()?.username
                            Log.d(TAG, "onResponse: ${username}")
                            textFindUserName.text = username
                            Toast.makeText(
                                this@UserFindNameActivity,
                                "아이디 찾기에 성공 했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@UserFindNameActivity,
                                "아이디 찾기에 실패 했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UserFindNameResponse>, t: Throwable) {
                        Toast.makeText(
                            this@UserFindNameActivity,
                            "네트워크 오류가 발생했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

            }




        }
    }
}