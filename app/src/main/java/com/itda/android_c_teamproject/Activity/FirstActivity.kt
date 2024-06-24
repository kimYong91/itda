package com.itda.android_c_teamproject.Activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.databinding.ActivityFirstBinding
import com.itda.android_c_teamproject.model.dto.UserDTO
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "FirstActivity"

class FirstActivity : AppCompatActivity() {
    lateinit var binding: ActivityFirstBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mainImage: ImageView
    var initTime = 0L
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

        val items = arrayOf("정보", "개인정보", "건강정보", "로그아웃")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        binding.spinner.adapter = adapter
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    1 -> {
                        startActivity(Intent(this@FirstActivity, UpdateUserPersonalActivity::class.java))
                    }
                    2 -> {
                        startActivity(Intent(this@FirstActivity, UpdateUserHealthActivity::class.java))
                    }
                    3 -> {
                        logout()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)

        val token = getToken()

        binding.run {
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this@FirstActivity, LoginActivity::class.java))
            }
            sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)

            // 로그인 시 저장된 사용자 이름을 가져옴
            val username = sharedPreferences.getString("username", "") ?: ""

            RetrofitClient.api.getUserHealthInfo("Bearer $token", username)
                .enqueue(object : Callback<UserDTO> {
                    override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                        if (response.isSuccessful) {
                            val user = response.body()

                            textName.text = "${username}님"
                            textAge.text = "나이 : ${user?.userAge.toString()}세"
                            textWeight.text = "몸무게 : ${user?.userWeight.toString()}kg"
                            textHeight.text = "키 : ${user?.userHeight.toString()}cm"
                            textBasalMetabolism.text =
                                "평균기초대사량 : ${user?.basalMetabolism.toString()}"

                        } else {
                            Log.d(TAG, "onResponse: 응답 실패 ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                        Log.d(TAG, "onFailure: 네트워크 실패")
                    }
                })


            textRecommendExercise.setOnClickListener {
                val intent = Intent(this@FirstActivity, ChatMainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@FirstActivity, "추천 운동 버튼 클릭됨", Toast.LENGTH_SHORT).show()
            }

            textFoodMenu.setOnClickListener {
                Toast.makeText(this@FirstActivity, "식단 버튼 클릭됨", Toast.LENGTH_SHORT).show()
            }

            val items2 = arrayOf("   유틸", "스탑워치", "카운터", "기능3")
            val adapter2 = ArrayAdapter(this@FirstActivity, android.R.layout.simple_spinner_item, items2)
            adapter2.setDropDownViewResource(R.layout.spinner_item2)
            binding.spinner2.adapter = adapter2
            binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        1 -> {
                            startActivity(Intent(this@FirstActivity, StopWatchActivity::class.java))
                        }

                        2 -> {
                            startActivity(Intent(this@FirstActivity, CounterActivity::class.java))
                        }

                        3 -> {

                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
            textGpt.setOnClickListener {
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