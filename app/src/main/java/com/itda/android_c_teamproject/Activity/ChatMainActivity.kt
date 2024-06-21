package com.itda.android_c_teamproject.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.BuildConfig
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.model.ChatRequest
import com.itda.android_c_teamproject.model.Response.ChatResponse
import com.itda.android_c_teamproject.model.Message
import com.itda.android_c_teamproject.network.ApiClient
import com.itda.android_c_teamproject.network.OpenAIService
import com.itda.android_c_teamproject.preferences.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatMainActivity : AppCompatActivity() {
    private lateinit var userInput: EditText
    private lateinit var sendButton: Button
    private lateinit var autoPromptButton1: Button
    private lateinit var autoPromptButton2: Button
    private lateinit var autoPromptButton3: Button
    private lateinit var autoPromptButton4: Button
    private lateinit var clearButton: Button
    private lateinit var stopButton: Button
    private lateinit var backButton: Button
    private lateinit var chatResponse: TextView
    private lateinit var loadingTextView: TextView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var errorMessage: TextView
    private var call: Call<ChatResponse>? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)

        // API Key를 로그로 출력
        Log.d("API_KEY_LOG", "API Key: ${BuildConfig.API_KEY}")

        userInput = findViewById(R.id.userInput)
        sendButton = findViewById(R.id.sendButton)
        autoPromptButton1 = findViewById(R.id.autoPromptButton1)
        autoPromptButton2 = findViewById(R.id.autoPromptButton2)
        autoPromptButton3 = findViewById(R.id.autoPromptButton3)
        autoPromptButton4 = findViewById(R.id.autoPromptButton4)
        clearButton = findViewById(R.id.clearButton)
        stopButton = findViewById(R.id.stopButton)
        backButton = findViewById(R.id.backButton)
        chatResponse = findViewById(R.id.chatResponse)
        loadingTextView = findViewById(R.id.loadingTextView)   // 실행 할 때 "요청중" 메세지
        loadingIndicator = findViewById(R.id.loadingIndicator)  // 실행할 때 로딩 이미지
        errorMessage = findViewById(R.id.errorMessage)

        sendButton.setOnClickListener {
            // 추가로 버튼 클릭 시에도 로그 출력 (필요시)
            Log.d("API_KEY_LOG", "Button clicked - API Key: ${BuildConfig.API_KEY}")

            val userMessage = userInput.text.toString()


            // chatResponse.text = "요청중"

            //  API 요청의 크기 조정
            // 입력 란에 아무런 명령이 입력 되지 않은 경우, 실행 되지 않고 입력 하라는 메세지 출력
            if (userMessage.isEmpty()) {
                errorMessage.text = "입력 되지 않았습니다"
                errorMessage.visibility = View.VISIBLE

                // 너무 긴 질문이 타임아웃을 유발할 수 있으므로, 질문을 적절한 길이로 분할하거나 트림
            } else if (userMessage.length > 1024) {  // OpenAI API에서 처리 가능한 최대 길이는 4096 tokens 이지만, 안전하게 1024로 설정
                chatResponse.text = "Error: Message is too long. Please shorten your input."
            } else {
                errorMessage.visibility = View.GONE
                sendMessageToChatGPT(userMessage)
            }
        }

        autoPromptButton1.setOnClickListener {
            try {
                val userInfo = UserPreferences.getUserInfo(this)
                val prompt = UserPreferences.createPrompt1(userInfo)

                // 로그 추가
                Log.d("AutoPrompt1", "Prompt: $prompt")

                userInput.setText(prompt)
            } catch (e: Exception) {
                Log.e("AutoPrompt1", "Error: ${e.message}")
                e.printStackTrace()
            }
            // sendMessageToChatGPT(prompt)
        }

        autoPromptButton2.setOnClickListener {
            try {
                val userInfo = UserPreferences.getUserInfo(this)
                val prompt = UserPreferences.createPrompt2(userInfo)

                // 로그 추가
                Log.d("AutoPrompt2", "Prompt: $prompt")

                userInput.setText(prompt)
            } catch (e: Exception) {
                Log.e("AutoPrompt2", "Error: ${e.message}")
                e.printStackTrace()
            }
            // sendMessageToChatGPT(prompt)
        }

        autoPromptButton3.setOnClickListener {
            try {
                val userInfo = UserPreferences.getUserInfo(this)
                val prompt = UserPreferences.createPrompt3(userInfo)

                // 로그 추가
                Log.d("AutoPrompt3", "Prompt: $prompt")

                userInput.setText(prompt)
            } catch (e: Exception) {
                Log.e("AutoPrompt3", "Error: ${e.message}")
                e.printStackTrace()
            }
            // sendMessageToChatGPT(prompt)
        }

        autoPromptButton4.setOnClickListener {
            try {
                val userInfo = UserPreferences.getUserInfo(this)
                val prompt = UserPreferences.createPrompt4(userInfo)

                // 로그 추가
                Log.d("AutoPrompt4", "Prompt: $prompt")

                userInput.setText(prompt)
            } catch (e: Exception) {
                Log.e("AutoPrompt4", "Error: ${e.message}")
                e.printStackTrace()
            }
            // sendMessageToChatGPT(prompt)
        }

        clearButton.setOnClickListener {
            userInput.text.clear()
            chatResponse.text = "" // 결과 창의 내용을 지운다.
            errorMessage.visibility = View.INVISIBLE
        }

        backButton.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }

        stopButton.setOnClickListener {
            call?.cancel() // 네트워크 호출 취소
            call = null // 새로운 요청을 받을 수 있게 초기화
            loadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
            loadingTextView.visibility = View.GONE // 요청중 숨기기
            chatResponse.text = "작업이 중지되었습니다."
        }

    }

    private fun sendMessageToChatGPT(message: String) {
        // 새로운 요청을 위한 call 초기화
        if (call != null && call!!.isExecuted) {
            call = null
        }

        loadingIndicator.visibility = View.VISIBLE // 로딩 인디케이터 표시
        loadingTextView.visibility = View.VISIBLE // 요청중 표시

        val apiService = ApiClient.retrofit.create(OpenAIService::class.java)
        val request = ChatRequest(
            model = "gpt-4",
            messages = listOf(Message(role = "user", content = message))
        )

        // 여기서 새 Call 객체를 생성
        call = apiService.getChatResponse(request)
        call?.enqueue(object : Callback<ChatResponse> {
            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                runOnUiThread {
                    loadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
                    loadingTextView.visibility = View.GONE // 요청중 숨기기
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
                    loadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
                    loadingTextView.visibility = View.GONE // 요청중 숨기기
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

}