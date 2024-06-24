package com.itda.android_c_teamproject.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itda.android_c_teamproject.BuildConfig
import com.itda.android_c_teamproject.R
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
    private lateinit var userInput: EditText
    private lateinit var sendButton: Button
    private lateinit var clearButton: Button
    private lateinit var stopButton: Button
    private lateinit var backButton: Button
    private lateinit var chatResponse: TextView
    private lateinit var loadingTextView: TextView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var errorMessage: TextView
    private var call: Call<ChatResponse>? = null

    private lateinit var userdto: UserDTO

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_chat)

        Log.d(TAG, "onCreate called")
        Log.d("API_KEY_LOG", "API Key: ${BuildConfig.API_KEY}")

        userInput = findViewById(R.id.userInput)
        sendButton = findViewById(R.id.sendButton)
        clearButton = findViewById(R.id.clearButton)
        stopButton = findViewById(R.id.stopButton)
        backButton = findViewById(R.id.backButton)
        chatResponse = findViewById(R.id.chatResponse)
        loadingTextView = findViewById(R.id.loadingTextView)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        errorMessage = findViewById(R.id.errorMessage)

        fetchUserDTOFromBackend()

        sendButton.setOnClickListener {
            val userMessage = userInput.text.toString()
            if (userMessage.isEmpty()) {
                errorMessage.text = "입력 되지 않았습니다"
                errorMessage.visibility = View.VISIBLE
            } else if (userMessage.length > 1024) {
                chatResponse.text = "Error: Message is too long. Please shorten your input."
            } else {
                errorMessage.visibility = View.GONE
                sendMessageToChatGPT(userMessage)
            }
        }

        clearButton.setOnClickListener {
            userInput.text.clear()
            chatResponse.text = ""
            errorMessage.visibility = View.INVISIBLE
        }

        backButton.setOnClickListener {
            finish()
        }

        stopButton.setOnClickListener {
            call?.cancel()
            call = null
            loadingIndicator.visibility = View.GONE
            loadingTextView.visibility = View.GONE
            chatResponse.text = "작업이 중지되었습니다."
        }
    }

    private fun sendMessageToChatGPT(message: String) {
        if (call != null && call!!.isExecuted) {
            call = null
        }

        loadingIndicator.visibility = View.VISIBLE
        loadingTextView.visibility = View.VISIBLE

        val apiService = ApiClient.retrofit.create(OpenAIService::class.java)
        val request = ChatRequest(
            model = "gpt-4",
            messages = listOf(Message(role = "user", content = message))
        )

        call = apiService.getChatResponse(request)
        call?.enqueue(object : Callback<ChatResponse> {
            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                runOnUiThread {
                    loadingIndicator.visibility = View.GONE
                    loadingTextView.visibility = View.GONE
                    if (call.isCanceled) {
                        chatResponse.text = "작업이 중지되었습니다."
                    } else {
                        chatResponse.text = "Error: ${t.message}"
                        Log.e("ChatGPT", "Error: ${t.printStackTrace()}")
                    }
                }
            }

            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                runOnUiThread {
                    loadingIndicator.visibility = View.GONE
                    loadingTextView.visibility = View.GONE
                    if (response.isSuccessful) {
                        val chatResponseData = response.body()
                        val reply = chatResponseData?.choices?.get(0)?.message?.content
                        chatResponse.text = reply
                    } else {
                        chatResponse.text = "Error: ${response.errorBody()?.string()}"
                        Log.e("ChatGPT", "Error: ${response.errorBody()?.string()}")
                    }
                }
            }
        })
    }

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
        })
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)
        return sharedPreferences.getString("token", null) ?: ""
    }

    private fun getUsername(): String {
        val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)
        return sharedPreferences.getString("username", null) ?: ""
    }
}