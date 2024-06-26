package com.itda.android_c_teamproject.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.BuildConfig
import com.itda.android_c_teamproject.databinding.ActivityPopupChatBinding
import com.itda.android_c_teamproject.model.ChatRequest
import com.itda.android_c_teamproject.model.Message
import com.itda.android_c_teamproject.model.Response.ChatResponse
import com.itda.android_c_teamproject.model.dto.UserDTO
import com.itda.android_c_teamproject.network.ApiClient
import com.itda.android_c_teamproject.network.OpenAIService
import com.itda.android_c_teamproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "PopupChatActivity"

class PopupChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPopupChatBinding
    private var call: Call<ChatResponse>? = null
    private lateinit var userdto: UserDTO
    var initTime = 0L

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate called")
        Log.d("API_KEY_LOG", "API Key: ${BuildConfig.API_KEY}")


        // 인텐트로 전달된 값을 사용하여 애니메이션을 적용
        binding.root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding.root.viewTreeObserver.removeOnPreDrawListener(this)

                val startX = intent.getIntExtra("startX", binding.root.width / 2)
                val startY = intent.getIntExtra("startY", binding.root.height / 2)
                val startWidth = intent.getIntExtra("startWidth", 0)
                val startHeight = intent.getIntExtra("startHeight", 0)

                val finalRadius = Math.hypot(binding.root.width.toDouble(), binding.root.height.toDouble()).toFloat()

                val circularReveal = ViewAnimationUtils.createCircularReveal(
                    binding.root, startX, startY, startWidth.toFloat(), finalRadius
                )
                circularReveal.duration = 1500 // 애니메이션 지속 시간 조절
                circularReveal.start()

                return true
            }
        })


        fetchUserDTOFromBackend()

        binding.sendButton.setOnClickListener {
            val userMessage = binding.userInput.text.toString()
            if (userMessage.isEmpty()) {
                binding.errorMessage.text = "입력 되지 않았습니다"
                binding.errorMessage.visibility = View.VISIBLE
            } else if (userMessage.length > 1024) {
                binding.chatResponse.text = "Error: Message is too long. Please shorten your input."
            } else {
                binding.errorMessage.visibility = View.GONE
                sendMessageToChatGPT(userMessage)
            }
        }

        binding.clearButton.setOnClickListener {
            binding.userInput.text.clear()
            binding.chatResponse.text = ""
            binding.errorMessage.visibility = View.INVISIBLE
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.stopButton.setOnClickListener {
            call?.cancel()
            call = null
            binding.loadingIndicator.visibility = View.GONE
            binding.loadingTextView.visibility = View.GONE
            binding.chatResponse.text = "작업이 중지되었습니다."
        }
    } // end onCreate

    private fun sendMessageToChatGPT(message: String) {
        if (call != null && call!!.isExecuted) {
            call = null
        }

        binding.loadingIndicator.visibility = View.VISIBLE
        binding.loadingTextView.visibility = View.VISIBLE

        val apiService = ApiClient.retrofit.create(OpenAIService::class.java)
        val request = ChatRequest(
            model = "gpt-4",
            messages = listOf(Message(role = "user", content = message))
        )

        call = apiService.getChatResponse(request)
        call?.enqueue(object : Callback<ChatResponse> {
            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                runOnUiThread {
                    binding.loadingIndicator.visibility = View.GONE
                    binding.loadingTextView.visibility = View.GONE
                    if (call.isCanceled) {
                        binding.chatResponse.text = "작업이 중지되었습니다."
                    } else {
                        binding.chatResponse.text = "Error: ${t.message}"
                        Log.e("ChatGPT", "Error: ${t.printStackTrace()}")
                    }
                }
            } // end onFailure

            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                runOnUiThread {
                    binding.loadingIndicator.visibility = View.GONE
                    binding.loadingTextView.visibility = View.GONE
                    if (response.isSuccessful) {
                        val chatResponseData = response.body()
                        val reply = chatResponseData?.choices?.get(0)?.message?.content
                        binding.chatResponse.text = reply
                    } else {
                        binding.chatResponse.text = "Error: ${response.errorBody()?.string()}"
                        Log.e("ChatGPT", "Error: ${response.errorBody()?.string()}")
                    }

                } // end runOnUiThread

            } // end onResponse

        }) // end call?

    } // end sendMessageToChatGPT

    private fun fetchUserDTOFromBackend() {
        var token = getToken()
        token = "Bearer $token"
        val userId = getUsername()
        Log.d(TAG, "Fetching UserDTO with token: $token and userId: $userId")
        RetrofitClient.api.getUserHealthInfo(token, userId).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    userdto = response.body()!!
                    Log.d(TAG, "UserDTO fetched: $userdto")
                } else {
                    Log.e(TAG, "Error fetching UserDTO: ${response.code()}")
                    Log.e(TAG, "Response message: ${response.message()}")
                    Log.e(TAG, "Response body: ${response.errorBody()?.string()}")
                }

            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                Log.e(TAG, "Error fetching UserDTO", t)
            }
        }) // end getUserHealthInfo
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)
        return sharedPreferences.getString("token", null) ?: ""
    }

    private fun getUsername(): String {
        val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)
        return sharedPreferences.getString("username", null) ?: ""
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