package com.itda.android_c_teamproject.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.network.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityLoginBinding
import com.itda.android_c_teamproject.model.LoginRequest
import com.itda.android_c_teamproject.model.Response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var initTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            textLogin.setOnClickListener {
                val username = editID.text.toString()
                val password = editPassword.text.toString()
                val user = LoginRequest(username, password)

                RetrofitClient.api.login(user).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "로그인 성공했습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("mylog", "onResponse: ${response.body()}")

                            // 변환된 JWT 토큰을 sharedPreferences 에 저장
                            val token = response.body()?.jwt ?: ""
                            val sharedPreferences =
                                getSharedPreferences("app_pref", Context.MODE_PRIVATE)

                            sharedPreferences.edit()
                                .putString("token", token)          // 토큰
                                .putString("username", username)    // 사용자 이름
                                .apply()

                            // 로그인 성공하면 메인 화면 진입
                            startActivity(Intent(this@LoginActivity, FirstActivity::class.java))

                            finish()    // 로그인 액티비티 종료


                        } else {
                            Toast.makeText(this@LoginActivity, "로그인 실패했습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("mylog", "onResponse: ${response.code()}")
                        }
                    } // end onResponse

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "로그인 네트워크 요청 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("mylog", "onFailure: ${t.message}")
                    }
                }) // end login
            } // end textLogin

            textRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            textFindUserName.setOnClickListener {
                startActivity(Intent(this@LoginActivity, FindUserNameActivity::class.java))
            }
            textFindPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, FindUserPasswordActivity::class.java))
            }
        } // end binding

    } // end onCreate
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