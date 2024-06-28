package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.network.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityRegisterBinding
import com.itda.android_c_teamproject.model.User
import com.itda.android_c_teamproject.model.dto.UserUsedNameDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private val handler = Handler(Looper.getMainLooper())
    private var checkIdRunnable: Runnable? = null
    var initTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {

            textExit.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            buttonMale.setOnClickListener {
                textGender.text = "남"
            }


            buttonFemale.setOnClickListener {
                textGender.text = "여"
            }

            // 아이디 입력 후 2초 후 존재하는 아이디인지 검사
            editId.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // 입력 전
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    checkIdRunnable?.let { handler.removeCallbacks(it) }
                    checkIdRunnable = Runnable {
                        val username = s.toString()
                        if (username.isNotBlank()) {
                            RetrofitClient.api.UserUsedName(username)
                                .enqueue(object : Callback<UserUsedNameDTO> {
                                    override fun onResponse(
                                        call: Call<UserUsedNameDTO>,
                                        response: Response<UserUsedNameDTO>
                                    ) {
                                        Log.d("RegisterActivity", "onResponse: ${response.body()}")
                                        if (response.isSuccessful) {
                                            val body = response.body()
                                            if (body != null) {
                                                editId.error = "이미 있는 아이디 입니다."
                                            } else {
                                                editId.error = null
                                            }
                                        } else {
                                            editId.error = null
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<UserUsedNameDTO>,
                                        t: Throwable
                                    ) {
                                        Log.d("RegisterActivity", "onFailure: ${t.message}")
                                        editId.error = "네트워크 오류"
                                    }
                                }) // end UserUsedName
                        } // end if
                    } // end checkIdRunnable
                    handler.postDelayed(checkIdRunnable!!, 2000) // 0.5초 후에 실행
                } // end onTextChanged

                override fun afterTextChanged(s: Editable?) {
                    // 입력 후
                }
            }) // end editId

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
            }) // end editDateOfBirth

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
                        editPhoneNumber.error = "숫자만 입력 가능합니다."
                    } else {
                        editDateOfBirth.error = null
                    }
                }
            }) // end editPhoneNumber

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
                    if (!s.isNullOrBlank() && s.length <= 8) {
                        editPassword.error = "비밀번호가 너무 짧습니다."
                    } else {
                        editPassword.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // 입력 후
                }
            }) // end editPassword

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
                    if (!s.isNullOrBlank() && !s.toString().contains('@') && !s.toString()
                            .contains('.')
                    ) {
                        editEmail.error = "정확한 이메일 주소를 입력해 주세요"
                    }
                }
            }) // end editEmail


            textRegister.setOnClickListener {
                val username = editId.text.toString()
                val password = editPassword.text.toString()
                val email = editEmail.text.toString()
                val phoneNumber = editPhoneNumber.text.toString()
                val dateOfBirth = editDateOfBirth.text.toString()
                val weight = editWeight.text.toString().toInt()
                val height = editHeight.text.toString().toDouble()
                val gender = textGender.text.toString()
                val user = User(
                    username,
                    password,
                    email,
                    phoneNumber,
                    dateOfBirth,
                    gender,
                    weight,
                    height
                )

                if (password.length in 8..13 && email.contains("@") && dateOfBirth.contains("-")) {
                    RetrofitClient.api.createUser(user).enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "회원가입에 성공하셨습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("mylog", "onResponse: ${response.body()}")
                                // 회원 가입 성공시 메시지 띄우고, 로그인 화면으로 이동
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        LoginActivity::class.java
                                    )
                                )
                            } else {
                                // 실패
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "화원가입에 실패하셨습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("mylog", "onResponse, 회원가입 실패: ${response.body()}")
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "회원가입 네트워크 요청 실패",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            Log.d("mylog", "onFailure: ${t.message}")
                        }
                    })
                } else if (email.contains("@")) {
                    Toast.makeText(this@RegisterActivity, "이메일을 정확하게 입력해주세요", Toast.LENGTH_SHORT)
                        .show()
                } else if (dateOfBirth.contains("-")) {
                    Toast.makeText(this@RegisterActivity, "생년월일을 정확하게 입력해주세요", Toast.LENGTH_SHORT)
                        .show()
                }
            } // end textRegister
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