package com.itda.android_c_teamproject.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.network.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityMainBinding
import com.itda.android_c_teamproject.model.UserDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = getToken()

        binding.run {
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            buttonLogout.setOnClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)
            buttonLogout.setOnClickListener {
                logout()
            }
            val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)

            // 로그인 시 저장된 사용자 이름을 가져옴
            val username = sharedPreferences?.getString("username", "") ?: ""

            RetrofitClient.api.getUserHealthInfo("Bearer ${getToken()}", username).enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        Log.d(TAG, "onResponse: ${user}")
                        textAge.text = user?.userAge.toString()
                        textWeight.text = user?.userWeight.toString()
                        textHeight.text = user?.userHeight.toString()
                        textBasalMetabolism.text = user?.userBasalMetabolism.toString()
                    } else {
                        Log.d(TAG, "onResponse: 응답 실패 ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Log.d(TAG, "onFailure: 네트워크 실패")
                }
            })
            textName.text = "${username}님"

        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)
        return sharedPreferences.getString("token", null) ?: ""
    }
    private fun logout() {
        // SharedPreferences에서 저장된 토큰과 사용자 이름 삭제
        sharedPreferences.edit()
            .remove("token")
            .remove("username")
            .apply()

        // 로그인 액티비티로 이동
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        // 현재 액티비티 종료
        finish()


    }
}