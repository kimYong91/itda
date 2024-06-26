package com.itda.android_c_teamproject.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.BuildConfig
import com.itda.android_c_teamproject.databinding.ActivityChatMainBinding
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


    private lateinit var binding: ActivityChatMainBinding
    private var call: Call<ChatResponse>? = null
    private lateinit var userdto: UserDTO
    var initTime = 0L

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그 추가: onCreate 메서드가 호출되었음을 확인
        Log.d(TAG, "onCreate called")

        // API Key를 로그로 출력
        Log.d("API_KEY_LOG", "API Key: ${BuildConfig.API_KEY}")

        // 백엔드에서 UserDTO 객체를 가져 오는 가정
        fetchUserDTOFromBackend()

        // 자동 프롬프트 버튼
        binding.autoPromptButton1.setOnClickListener {
            Log.d(TAG, "AutoPrompt1 clicked")
                if (::userdto.isInitialized) {  // userdto가 초기화되었는지 확인
                    // val userInfo = UserPreferences.getUserInfo(this)
                    val exerciseType = binding.exerciseTypeInput.text.toString()
                    val exerciseDurationTime = binding.exerciseDurationTimeInput.text.toString()
                    val exerciseDurationDay = binding.exerciseDurationDayInput.text.toString()
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


        // 모두 삭제 버튼
        binding.clearButton.setOnClickListener {
            binding.exerciseTypeInput.text.clear()
            binding.exerciseDurationTimeInput.text.clear()
            binding.exerciseDurationDayInput.text.clear()
            binding.chatResponse.text = "" // 결과 창의 내용을 지운다.
            binding.errorMessage.visibility = View.INVISIBLE
        }

        // 뒤로 가기 버튼
        binding.backButton.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }

        // 중지 버튼
        binding.stopButton.setOnClickListener {
            call?.cancel() // 네트워크 호출 취소
            call = null // 새로운 요청을 받을 수 있게 초기화
            binding.loadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
            binding.loadingTextView.visibility = View.GONE // 요청중 숨기기
            binding.chatResponse.text = "작업이 중지되었습니다."
        }
    }

    // 프롬프트 gpt 전달
    private fun sendMessageToChatGPT(message: String) {
        // 새로운 요청을 위한 call 초기화
        if (call != null && call!!.isExecuted) {
            call = null
        }

        binding.loadingIndicator.visibility = View.VISIBLE // 로딩 인디케이터 표시
        binding.loadingTextView.visibility = View.VISIBLE // 요청중 표시

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
                    binding.loadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
                    binding.loadingTextView.visibility = View.GONE // 요청중 숨기기
                    if (call.isCanceled) {
                        binding.chatResponse.text = "작업이 중지되었습니다."
                    } else {
                        binding.chatResponse.text = "Error: ${t.message}"
                        Log.e("ChatGPT", "Error: ${t.printStackTrace()}")
                    }
                }
            }

            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                runOnUiThread {
                    binding.loadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
                    binding.loadingTextView.visibility = View.GONE // 요청중 숨기기
                    if (response.isSuccessful) {
                        val chatResponseData = response.body()
                        val reply = chatResponseData?.choices?.get(0)?.message?.content
                        binding.chatResponse.text = reply
                    } else {
                        binding.chatResponse.text = "Error: ${response.errorBody()?.string()}"
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




