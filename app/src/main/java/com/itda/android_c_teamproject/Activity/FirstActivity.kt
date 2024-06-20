package com.itda.android_c_teamproject.Activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.databinding.ActivityFirstBinding
import com.itda.android_c_teamproject.model.UserDTO
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "FirstActivity"

class FirstActivity : AppCompatActivity() {
    lateinit var binding: ActivityFirstBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mainImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainImage = findViewById(R.id.mainImage)

        // 가로 모드인지 확인하고 이미지의 가시성을 설정합니다
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mainImage.visibility = ImageView.GONE
        } else {
            mainImage.visibility = ImageView.VISIBLE
        }


        sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)

        val token = getToken()

        binding.run {
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this@FirstActivity, LoginActivity::class.java))
            }
            buttonLogout.setOnClickListener {
                startActivity(Intent(this@FirstActivity, LoginActivity::class.java))
            }
            sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)
            buttonLogout.setOnClickListener {
                logout()
            }

            val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)

            // 로그인 시 저장된 사용자 이름을 가져옴
            val username = sharedPreferences.getString("username", "") ?: ""

            RetrofitClient.api.getUserHealthInfo("Bearer $token", username).enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        Log.d(TAG, "onResponse: ${user}")
                        textAge.text = user?.userAge.toString()
                        textWeight.text = user?.userWeight.toString()
                        textHeight.text = user?.userHeight.toString()
                        Log.d(TAG, "onResponse: ${user?.basalMetabolism.toString() ?: "빈데이터"}")
                        textBasalMetabolism.text = user?.basalMetabolism.toString()
                    } else {
                        Log.d(TAG, "onResponse: 응답 실패 ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Log.d(TAG, "onFailure: 네트워크 실패")
                }
            })
            textName.text = "${username}님"

            recommendExerciseButton.setOnClickListener{
                val intent = Intent(this@FirstActivity, ChatMainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@FirstActivity, "추천 운동 버튼 클릭됨", Toast.LENGTH_SHORT).show()
            }

            foodMenuButton.setOnClickListener{
                Toast.makeText(this@FirstActivity, "식단 버튼 클릭됨", Toast.LENGTH_SHORT).show()
            }

            stopwatchButton.setOnClickListener{
                Toast.makeText(this@FirstActivity, "스탑워치 버튼 클릭됨", Toast.LENGTH_SHORT).show()
            }

            gptButton.setOnClickListener{
                Toast.makeText(this@FirstActivity, "GPT 버튼 클릭됨", Toast.LENGTH_SHORT).show()
            }
        }
    }




//        val recommendExerciseButton: Button = findViewById(R.id.recommendExerciseButton)
//
//        val foodMenuButton: Button = findViewById(R.id.foodMenuButton)
//
//        val stopwatchButton: Button = findViewById(R.id.stopwatchButton)
//
//        val gptButton: Button = findViewById(R.id.gptButton)
//
//
//        recommendExerciseButton.setOnClickListener{
//            val intent = Intent(this, ChatMainActivity::class.java)
//            startActivity(intent)
//            Toast.makeText(this, "추천 운동 버튼 클릭됨", Toast.LENGTH_SHORT).show()
//        }
//
//        foodMenuButton.setOnClickListener{
//            Toast.makeText(this, "식단 버튼 클릭됨", Toast.LENGTH_SHORT).show()
//        }
//
//        stopwatchButton.setOnClickListener{
//            Toast.makeText(this, "스탑워치 버튼 클릭됨", Toast.LENGTH_SHORT).show()
//        }
//
//        gptButton.setOnClickListener{
//            Toast.makeText(this, "GPT 버튼 클릭됨", Toast.LENGTH_SHORT).show()
//        }
//
//    }

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
        val intent = Intent(this@FirstActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        // 현재 액티비티 종료
        finish()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // 가로/세로 모드 변경 시 이미지의 가시성을 설정합니다
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mainImage.visibility = ImageView.GONE
        } else {
            mainImage.visibility = ImageView.VISIBLE
        }
    }

}