package com.itda.android_c_teamproject.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityUpdateUserHealthBinding
import com.itda.android_c_teamproject.model.User
import com.itda.android_c_teamproject.model.dto.UserHealthDTO
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
    var initTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserHealthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)
        val token = getToken()

        binding.run {

            val username = sharedPreferences.getString("username", "") ?: ""

            textExit.setOnClickListener {
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
            textUpdate.setOnClickListener {
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

                        }) // end updateUserHealthInfo
            } // end textUpdate
        } // end binding

    } // end onCreate

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)
        return sharedPreferences.getString("token", null) ?: ""
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 뒤로가기 버튼을 누른지 3초 이내가 아니거나 처음 누를 경우
            if (System.currentTimeMillis() - initTime > 3000) {
                Toast.makeText(this, "종료하려면 한 번 더 누르세요.", Toast.LENGTH_SHORT).show()
                initTime = System.currentTimeMillis()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}