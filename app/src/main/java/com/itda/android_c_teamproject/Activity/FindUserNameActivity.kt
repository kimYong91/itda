package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.network.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityFindUserNameBinding
import com.itda.android_c_teamproject.model.dto.UserFindNameDTO
import com.itda.android_c_teamproject.model.Response.UserFindNameResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserFindNameActivity"

class FindUserNameActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindUserNameBinding
    var initTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            buttonExit.setOnClickListener {
                startActivity(Intent(this@FindUserNameActivity, LoginActivity::class.java))
            }

            buttonFindUserName.setOnClickListener {
                val email = editEmail.text.toString()
                val phoneNumber = editPhoneNumber.text.toString()
                val dateOfBirth = editDateOfBirth.text.toString()

                if (email.isEmpty() || phoneNumber.isEmpty() || dateOfBirth.isEmpty()) {
                    Toast.makeText(
                        this@FindUserNameActivity,
                        "모든 정보를 입력해주세요",
                        Toast.LENGTH_SHORT
                        ).show()
                }

                val userFindNameDTO = UserFindNameDTO(email, phoneNumber, dateOfBirth)

                RetrofitClient.api.findUsername(userFindNameDTO).enqueue(object :
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
                                this@FindUserNameActivity,
                                "아이디 찾기에 성공 했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@FindUserNameActivity,
                                "아이디 찾기에 실패 했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UserFindNameResponse>, t: Throwable) {
                        Toast.makeText(
                            this@FindUserNameActivity,
                            "네트워크 오류가 발생했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

            }




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