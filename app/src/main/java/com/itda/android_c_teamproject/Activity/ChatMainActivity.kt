package com.itda.android_c_teamproject.Activity

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.BuildConfig
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.model.ChatRequest
import com.itda.android_c_teamproject.model.Response.ChatResponse
import com.itda.android_c_teamproject.model.Message
import com.itda.android_c_teamproject.model.dto.UserDTO
import com.itda.android_c_teamproject.network.ApiClient
import com.itda.android_c_teamproject.network.OpenAIService
import com.itda.android_c_teamproject.network.RetrofitClient
import com.itda.android_c_teamproject.preferences.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ChatMainActivity"

class ChatMainActivity : AppCompatActivity() {
    //private lateinit var userInput: EditText
    private lateinit var exerciseTypeInput: EditText
    private lateinit var exerciseDurationTimeInput: EditText
    private lateinit var exerciseDurationDayInput: EditText
//    private lateinit var sendButton: Button
    private lateinit var autoPromptButton1: Button
    private lateinit var clearButton: Button
    private lateinit var stopButton: Button
    private lateinit var backButton: Button
    private lateinit var chatResponse: TextView
    private lateinit var loadingTextView: TextView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var errorMessage: TextView
    private var call: Call<ChatResponse>? = null

    private lateinit var userdto: UserDTO
    var initTime = 0L

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)



        // 로그 추가: onCreate 메서드가 호출되었음을 확인
        Log.d(TAG, "onCreate called")

        // API Key를 로그로 출력
        Log.d("API_KEY_LOG", "API Key: ${BuildConfig.API_KEY}")

        //userInput = findViewById(R.id.userInput)
        exerciseTypeInput = findViewById(R.id.exerciseTypeInput)
        exerciseDurationTimeInput = findViewById(R.id.exerciseDurationTimeInput)
        exerciseDurationDayInput = findViewById(R.id.exerciseDurationDayInput)
//        sendButton = findViewById(R.id.sendButton)
        autoPromptButton1 = findViewById(R.id.autoPromptButton1)
        clearButton = findViewById(R.id.clearButton)
        stopButton = findViewById(R.id.stopButton)
        backButton = findViewById(R.id.backButton)
        chatResponse = findViewById(R.id.chatResponse)
        loadingTextView = findViewById(R.id.loadingTextView)   // 실행 할 때 "요청중" 메세지
        loadingIndicator = findViewById(R.id.loadingIndicator)  // 실행할 때 로딩 이미지
        errorMessage = findViewById(R.id.errorMessage)

        // 백엔드에서 UserDTO 객체를 가져 오는 가정
        fetchUserDTOFromBackend()

//        sendButton.setOnClickListener {
//            // 추가로 버튼 클릭 시에도 로그 출력 (필요시)
//            Log.d("API_KEY_LOG", "Button clicked - API Key: ${BuildConfig.API_KEY}")
//
//            val userMessage = userInput.text.toString()
//
//
//            // chatResponse.text = "요청중"
//
//            //  API 요청의 크기 조정
//            // 입력 란에 아무런 명령이 입력 되지 않은 경우, 실행 되지 않고 입력 하라는 메세지 출력
//            if (userMessage.isEmpty()) {
//                errorMessage.text = "입력 되지 않았습니다"
//                errorMessage.visibility = View.VISIBLE
//
//                // 너무 긴 질문이 타임아웃을 유발할 수 있으므로, 질문을 적절한 길이로 분할하거나 트림
//            } else if (userMessage.length > 1024) {  // OpenAI API에서 처리 가능한 최대 길이는 4096 tokens 이지만, 안전하게 1024로 설정
//                chatResponse.text = "Error: Message is too long. Please shorten your input."
//            } else {
//                errorMessage.visibility = View.GONE
//                sendMessageToChatGPT(userMessage)
//            }
//        }


//        autoPromptButton1.setOnClickListener {
//            try {
//                val prompt = UserPreferences.createPrompt1(userdto)
//
//                // 로그 추가
//                Log.d("AutoPrompt1", "Prompt: $prompt")
//
//                userInput.setText(prompt)
//            } catch (e: Exception) {
//                Log.e("AutoPrompt1", "Error: ${e.message}")
//                e.printStackTrace()
//            }
//            // sendMessageToChatGPT(prompt)
//        }

        // 자동 프롬프트 버튼
        autoPromptButton1.setOnClickListener {
            Log.d(TAG, "AutoPrompt1 clicked")
                if (::userdto.isInitialized) {  // userdto가 초기화되었는지 확인
                    // val userInfo = UserPreferences.getUserInfo(this)
                    val exerciseType = exerciseTypeInput.text.toString()
                    val exerciseDurationTime = exerciseDurationTimeInput.text.toString()
                    val exerciseDurationDay = exerciseDurationDayInput.text.toString()
                    val prompt = UserPreferences.createPrompt1(userdto, exerciseType, exerciseDurationTime, exerciseDurationDay)
                    // 로그 추가: 프롬프트 생성 확인
                    Log.d(TAG, "Prompt: $prompt")
                    sendMessageToChatGPT(prompt)
                    // userInput.setText(prompt) // EditText에 설정
                } else {
                    // 로그 추가: userdto 초기화되지 않음
                    Log.e(TAG, "userdto is not initialized")
                }



        }


//        autoPromptButton2.setOnClickListener {
//            Log.d(TAG, "AutoPrompt2 clicked")
//            if (::userdto.isInitialized) {
//            val prompt = UserPreferences.createPrompt2(userdto)
//            userInput.setText(prompt)
//        } else {
//                Log.e(TAG, "userdto is not initialized")
//        }
//    }
//

        // 모두 삭제 버튼
        clearButton.setOnClickListener {
            //userInput.text.clear()
            exerciseTypeInput.text.clear()
            exerciseDurationTimeInput.text.clear()
            exerciseDurationDayInput.text.clear()

            chatResponse.text = "" // 결과 창의 내용을 지운다.
            errorMessage.visibility = View.INVISIBLE
        }

        // 뒤로 가기 버튼
        backButton.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }

        // 중지 버튼
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
        //val apiService = ApiClient.apiService
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

    // 백엔드 통합:

    //fetchUserDTOFromBackend 메서드,  이 함수는 UserDTO 객체를 백엔드에서 가져오는 역할
    //이 객체를 userdto 변수에 저장하여 버튼 클릭 리스너에서 사용할 수 있도록 합니다.
    private fun fetchUserDTOFromBackend() {
        var token = getToken() // 실제 토큰 값으로 변경
        token = "Bearer ${token}"
        val userId = getUsername() // 실제 사용자 ID로 변경
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




