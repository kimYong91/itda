package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityFindUserPasswordBinding
import com.itda.android_c_teamproject.model.dto.UserFindPasswordDTO
import com.itda.android_c_teamproject.model.Response.UserFindPasswordResponse
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindUserPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindUserPasswordBinding
    var initTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            textExit.setOnClickListener {
                startActivity(Intent(this@FindUserPasswordActivity, LoginActivity::class.java))
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
                    // 입력 중
                    s?.let { number ->
                        val formattedNumber = formatDateString(number.toString())
                        if (editDateOfBirth.text.toString() != formattedNumber) {
                            editDateOfBirth.setText(formattedNumber)
                            editDateOfBirth.setSelection(formattedNumber.length) // 커서 위치 조정
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
                    if (!s.isNullOrBlank() && !s.all { it.isDigit() }) {
                        editDateOfBirth.error = "숫자만 입력 가능합니다."
                    } else {
                        editDateOfBirth.error = null
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


            textFindingPassword.setOnClickListener {

                val username = editUsername.text.toString()
                val email = editEmail.text.toString()
                val phoneNumber = editPhoneNumber.text.toString()
                val dateOfBirth = editDateOfBirth.text.toString()

                if (username.isBlank() || email.isBlank() || phoneNumber.isBlank() || dateOfBirth.isBlank()) {
                    Toast.makeText(
                        this@FindUserPasswordActivity,
                        "모든 정보를 입력해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val userFindPasswordDTO =
                    UserFindPasswordDTO(username, email, phoneNumber, dateOfBirth)

                RetrofitClient.api.findUserPassword(userFindPasswordDTO)
                    .enqueue(object : Callback<UserFindPasswordResponse> {
                        override fun onResponse(
                            call: Call<UserFindPasswordResponse>,
                            response: Response<UserFindPasswordResponse>
                        ) {
                            if (response.isSuccessful) {
                                val newPassword = response.body()?.newPassword
                                textFindPassword.text = newPassword
                                Toast.makeText(
                                    this@FindUserPasswordActivity,
                                    "임시 비밀번호 생성 성공 했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@FindUserPasswordActivity,
                                    "비밀번호 찾기에 실패했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<UserFindPasswordResponse>, t: Throwable) {
                            Toast.makeText(
                                this@FindUserPasswordActivity,
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

    private fun formatDateString(input: String): String {
        val number = input.replace("-", "") // 입력된 문자열에서 "-" 제거
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