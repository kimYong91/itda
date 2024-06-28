package com.itda.android_c_teamproject.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityDeleteUserBinding
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteUserActivity : AppCompatActivity() {
    private val TAG = "DeleteUserActivity"
    private lateinit var binding: ActivityDeleteUserBinding
    private lateinit var sharedPreferences: SharedPreferences
    var initTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)
        val token = getToken()
        val username = sharedPreferences.getString("username", "") ?: ""

        binding.run {
            textDelete.setOnClickListener {
                RetrofitClient.api.userDelete("Bearer ${token}", username).enqueue(object :
                    Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@DeleteUserActivity,
                                "${username}님이 탈퇴 되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@DeleteUserActivity,
                                    LoginActivity::class.java
                                )
                            )
                            Log.d(TAG, "onResponse: ${username}삭제 되었습니다.")
                        } else {
                            Log.d(TAG, "onResponse: 없는 아이디 입니다.")
                            Toast.makeText(
                                this@DeleteUserActivity,
                                "없는 아이디 입니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d(TAG, "onFailure: 네트워크 연결에 실패하였습니다.")
                        Toast.makeText(
                            this@DeleteUserActivity,
                            "네트워크 연결에 실패하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }) // end RetrofitClient

            } // end textDelete

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