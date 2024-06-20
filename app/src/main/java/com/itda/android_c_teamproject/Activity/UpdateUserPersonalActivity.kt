package com.itda.android_c_teamproject.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.databinding.ActivityUpdateUserPersonalBinding
import com.itda.android_c_teamproject.model.UserPersonalDTO
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UpdateUserPersonalActiv"
class UpdateUserPersonalActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateUserPersonalBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)
        val token = getToken()


        binding.run {

            buttonExit.setOnClickListener {
                startActivity(Intent(this@UpdateUserPersonalActivity, FirstActivity::class.java))
            }

            buttonUpdate.setOnClickListener {
                val newUsername = editUsername.text.toString()
                val newPassword = editPassword.text.toString()
                val newEmail = editEmail.text.toString()
                val newPhoneNumber = editPhoneNumber.text.toString()
                val newDateOfBirth = editDateOfBirth.text.toString()
                val userPersonalDTO = UserPersonalDTO(
                    newUsername,
                    newPassword,
                    newEmail,
                    newPhoneNumber,
                    newDateOfBirth
                )

                val username = sharedPreferences.getString("username", "") ?: ""

                RetrofitClient.api.updateUserPersonalDTO("Bearer $token", username, userPersonalDTO)
                    .enqueue(
                        object : Callback<UserPersonalDTO> {
                            override fun onResponse(
                                call: Call<UserPersonalDTO>,
                                response: Response<UserPersonalDTO>
                            ) {
                                if (response.isSuccessful) {
                                    val user = response.body()

                                    textUsername.text = "아이디 : ${username}"
                                    textEmail.text = "이메일 : ${user?.email}"
                                    textPhoneNumber.text = "핸드폰 번호 : ${user?.phoneNumber}"
                                    textDateOfBirth.text = "생년월일 : ${user?.dateOfBirth}"


                                }
                            }

                            override fun onFailure(call: Call<UserPersonalDTO>, t: Throwable) {
                                Log.d(TAG, "onFailure: 네트워크 실패")
                            }
                        })
            }
        }

    }
    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)
        return sharedPreferences.getString("token", null) ?: ""
    }
}