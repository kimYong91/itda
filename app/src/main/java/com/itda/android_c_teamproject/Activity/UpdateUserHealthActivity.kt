package com.itda.android_c_teamproject.Activity

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.databinding.ActivityUpdateUserHealthBinding
import com.itda.android_c_teamproject.model.User
import com.itda.android_c_teamproject.model.UserHealthDTO
import com.itda.android_c_teamproject.model.UserPersonalDTO
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UpdateUserHealthActivity"

class UpdateUserHealthActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateUserHealthBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var userHealthDTO: UserHealthDTO
    lateinit var gender: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserHealthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)
        val token = getToken()

        binding.run {

            val username = sharedPreferences.getString("username", "") ?: ""

            buttonExit.setOnClickListener {
                startActivity(Intent(this@UpdateUserHealthActivity, FirstActivity::class.java))
                finish()
            }

            RetrofitClient.api.getUserInfo("Bearer $token", username).enqueue(object :
                Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val user = response.body()
                    textUsername.text = "${username}님"
                    textGender.text = "성별 : ${user?.userGender.toString()}"
                    textWeight.text = "몸무게 : ${user?.userWeight.toString()}"
                    textHeight.text = "키 : ${user?.userHeight.toString()}"
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(TAG, "onFailure: 네트워크 요청 실패")
                }

            })

            buttonMale.setOnClickListener {
                gender = "남"
            }
            buttonFemale.setOnClickListener {
                gender = "여"
            }
            buttonUpdate.setOnClickListener {
                val weight = editWeight.text.toString().toIntOrNull()
                val height = editHeight.text.toString().toDoubleOrNull()

                userHealthDTO = UserHealthDTO(gender, weight, height)

                RetrofitClient.api.updateUserHealthInfo("Bearer ${token}", username, userHealthDTO)
                    .enqueue(
                        object : Callback<UserHealthDTO> {
                            override fun onResponse(
                                call: Call<UserHealthDTO>,
                                response: Response<UserHealthDTO>
                            ) {
                                if (response.isSuccessful) {
                                    Log.d(TAG, "onResponse: 정보 수정 성공 ${response.code()}")
                                    Toast.makeText(
                                        this@UpdateUserHealthActivity,
                                        "정보를 수정 하였습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val user = response.body()
                                    textGender.text = "성별 : ${user?.userGender.toString()}"
                                    textWeight.text = "몸무게 : ${user?.userWeight.toString()}"
                                    textHeight.text = "키 : ${user?.userHeight.toString()}"
                                } else {
                                    Log.d(TAG, "onResponse: 정보 수정 실패 ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<UserHealthDTO>, t: Throwable) {
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