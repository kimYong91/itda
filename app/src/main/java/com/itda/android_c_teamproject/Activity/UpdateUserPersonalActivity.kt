package com.itda.android_c_teamproject.Activity

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.itda.android_c_teamproject.databinding.ActivityUpdateUserPersonalBinding
import com.itda.android_c_teamproject.model.User
import com.itda.android_c_teamproject.model.dto.UserPersonalDTO
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UpdateUserPersonalActivity"

class UpdateUserPersonalActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateUserPersonalBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var userPersonalDTO: UserPersonalDTO
    var initTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)
        val token = getToken()

        binding.run {

            val username = sharedPreferences.getString("username", "") ?: ""

            textExit.setOnClickListener {
                startActivity(Intent(this@UpdateUserPersonalActivity, FirstActivity::class.java))
                finish()
            }

            // 생년월일 입력시 '-' 자동 생성, 문자 입력시 에러 메시지 생성
            editDateOfBirth.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // 입력 전
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let { number ->
                        val formattedNumber = formatDateString(number.toString())
                        if (editDateOfBirth.text.toString() != formattedNumber) {
                            editDateOfBirth.setText(formattedNumber)
                            editDateOfBirth.setSelection(formattedNumber.length)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrBlank() && !s.all { it.isDigit() || it == '-' }) {
                        editDateOfBirth.error = "숫자만 입력 가능합니다."
                    } else {
                        editDateOfBirth.error = null
                    }
                }
            })

            // 핸드폰 번호 입력시 숫자만 입력 가능, 문자 입력시 에러 메시지 생성
            editPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // 입력 전
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 입력 중
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrBlank() && !s.all { it.isDigit()}) {
                        editDateOfBirth.error = "숫자만 입력 가능합니다."
                    } else {
                        editDateOfBirth.error = null
                    }
                }
            })

            // 비밀번호 8글자 이하 입력시 에러메시지 생성
            editPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // 입력 전
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 입력 중
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrBlank() && s.length <= 8) {
                        editPassword.error = "비밀번호가 너무 짧습니다."
                    } else {
                        editPassword.error = null
                    }
                }
            })

            // 이메일에 '@', '.' 불포함시 에러메시지
            editEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // 입력 전
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 입력 중
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrBlank() && !s.toString().contains('@') && !s.toString().contains('.')) {
                        editEmail.error = "정확한 이메일 주소를 입력해 주세요"
                    }
                }
            })


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


            textUpdate.setOnClickListener {

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

    private fun formatDateString(input: String): String {
        val number = input.replace("-", "")
        val sb = StringBuilder(number)
        if (sb.length > 4) {
            sb.insert(4, "-")
        }
        if (sb.length > 7) {
            sb.insert(7, "-")
        }
        return sb.toString()
    }
}