package com.itda.android_c_teamproject.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityChatMainBinding
import com.itda.android_c_teamproject.BuildConfig
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.model.ChatRequest
import com.itda.android_c_teamproject.model.Message
import com.itda.android_c_teamproject.model.Response.ChatResponse
import com.itda.android_c_teamproject.model.dto.UserDTO
import com.itda.android_c_teamproject.network.ApiClient
import com.itda.android_c_teamproject.network.OpenAIService
import com.itda.android_c_teamproject.network.RetrofitClient
import com.itda.android_c_teamproject.preferences.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileNotFoundException


private const val TAG = "ChatMainActivity"

class ChatMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatMainBinding
    private var call: Call<ChatResponse>? = null
    private lateinit var userdto: UserDTO
    var initTime = 0L
    private lateinit var selectedExerciseDurationDayInput: String
    private lateinit var selectedExerciseDurationTimeInput: String
    private lateinit var selectedJob: String
    private lateinit var selectedDailyFoodIntake: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그 추가: onCreate 메서드가 호출되었음을 확인
        Log.d(TAG, "onCreate called")

        // API Key를 로그로 출력



        binding.run {

            // 백엔드에서 UserDTO 객체를 가져 오는 가정
            fetchUserDTOFromBackend()


            val itemsExerciseDay =
                arrayOf("운동 일수", "1일", "2일", "3일", "4일", "5일", "6일", "7일")
            val adapterExerciseDay =
                ArrayAdapter(
                    this@ChatMainActivity,
                    android.R.layout.simple_spinner_item,
                    itemsExerciseDay
                )
            adapterExerciseDay.setDropDownViewResource(R.layout.spinner_item2)
            exerciseDurationDayInput.adapter = adapterExerciseDay

            exerciseDurationDayInput.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedExerciseDurationDayInput = when (position) {
                            1 -> "1"
                            2 -> "2"
                            3 -> "3"
                            4 -> "4"
                            5 -> "5"
                            6 -> "6"
                            7 -> "7"
                            else -> ""
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                } // exerciseDurationDayInput


            val itemsExerciseTime = arrayOf(
                "운동 시간",
                "1시간",
                "2시간",
                "3시간",
                "4시간",
                "5시간",
                "6시간",
                "7시간",
                "8시간",
                "9시간",
                "10시간",
                "11시간",
                "12시간"
            )
            val adapterExerciseTime =
                ArrayAdapter(
                    this@ChatMainActivity,
                    android.R.layout.simple_spinner_item,
                    itemsExerciseTime
                )
            adapterExerciseTime.setDropDownViewResource(R.layout.spinner_item2)
            exerciseDurationTimeInput.adapter = adapterExerciseTime
            exerciseDurationTimeInput.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedExerciseDurationTimeInput = when (position) {
                            1 -> "1"
                            2 -> "2"
                            3 -> "3"
                            4 -> "4"
                            5 -> "5"
                            6 -> "6"
                            7 -> "7"
                            8 -> "8"
                            9 -> "9"
                            10 -> "10"
                            11 -> "11"
                            12 -> "12"
                            else -> ""
                        }
                    } // end onItemSelected

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                } // exerciseDurationTimeInput


            val itemsJob = arrayOf(
                "직업",
                "사무직, 활동 없음",
                "부분 활동 서비스직",
                "주 활동 서비스직",
                "육체 노동"
            )
            val adapterJob =
                ArrayAdapter(
                    this@ChatMainActivity,
                    android.R.layout.simple_spinner_item,
                    itemsJob
                )
            adapterJob.setDropDownViewResource(R.layout.spinner_item2)
            job.adapter = adapterJob
            job.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedJob = when (position) {
                            1 -> "사무직, 활동 없음"
                            2 -> "부분 활동 서비스직"
                            3 -> "주 활동 서비스직"
                            4 -> "육체 노동"
                            else -> ""
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                } // job


            val itemsDailyFoodIntake = arrayOf(
                "하루 식사",
                "1회",
                "2회",
                "3회",
                "4회",
                "5회"
            )

            val adapterDailyFoodIntake =
                ArrayAdapter(
                    this@ChatMainActivity,
                    android.R.layout.simple_spinner_item,
                    itemsDailyFoodIntake
                )
            adapterDailyFoodIntake.setDropDownViewResource(R.layout.spinner_item2)
            dailyFoodIntake.adapter = adapterDailyFoodIntake
            dailyFoodIntake.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedDailyFoodIntake = when (position) {
                            1 -> "1끼"
                            2 -> "2끼"
                            3 -> "3끼"
                            4 -> "4끼"
                            5 -> "5끼"
                            else -> ""
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                } // dailyFoodIntake


            // 자동 프롬프트 버튼
            autoPromptButton1.setOnClickListener {

                if (::userdto.isInitialized) {
                    val exerciseType = exerciseTypeInput.text.toString()
                    val exercisePreference = editExercisePreference.text.toString()
                    val exerciseGoal = editExerciseGoal.text.toString()
                    val exerciseFacility = editExerciseFacility.text.toString()
                    val health = editHealth.text.toString()

                    val prompt = UserPreferences.createPrompt1(
                        userdto,
                        selectedJob,
                        health,
                        exercisePreference,
                        selectedDailyFoodIntake,
                        exerciseGoal,
                        exerciseFacility,
                        exerciseType,
                        selectedExerciseDurationTimeInput,
                        selectedExerciseDurationDayInput
                    ) // end prompt

                    // 로그 추가: 프롬프트 생성 확인
                    Log.d(TAG, "Prompt: $prompt")
                    sendMessageToChatGPT(prompt)
                    // userInput.setText(prompt) // EditText에 설정
                } else {
                    // 로그 추가: userdto 초기화되지 않음
                    Log.e(TAG, "userdto is not initialized")
                }

            } // end autoPromptButton1

            clearButton.setOnClickListener {
                editHealth.text.clear()
                editExercisePreference.text.clear()
                editExerciseGoal.text.clear()
                editExerciseFacility.text.clear()
                exerciseTypeInput.text.clear()

                chatResponse.text = "" // 결과 창의 내용을 지운다.
                errorMessage.visibility = View.INVISIBLE
            } // end clearButton

            // 뒤로 가기 버튼
            backButton.setOnClickListener {
                val intent = Intent(this@ChatMainActivity, FirstActivity::class.java)
                startActivity(intent)
            } // end backButton

            // 중지 버튼
            stopButton.setOnClickListener {
                call?.cancel() // 네트워크 호출 취소
                call = null // 새로운 요청을 받을 수 있게 초기화
                loadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
                loadingTextView.visibility = View.GONE // 요청중 숨기기
                chatResponse.text = "작업이 중지되었습니다."

            } // end stopButton

        } // end binding

    } // end onCreate


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

                } // end runOnUiThread

            } // end onFailure

            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                runOnUiThread {
                    binding.loadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
                    binding.loadingTextView.visibility = View.GONE // 요청중 숨기기
                    if (response.isSuccessful) {
                        val chatResponseData = response.body()
                        val reply = chatResponseData?.choices?.get(0)?.message?.content
                        binding.chatResponse.text = reply

                        val fileName = "itda.txt"   // 저장할 파일 이름

                        val inputData = reply.toString()

                        try {

                            val fileOutput = openFileOutput(fileName, MODE_PRIVATE)
                            fileOutput.write(inputData.toByteArray())
                            fileOutput.close()
                            Log.d("FileIO", "파일 쓰기 성공: $fileName")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e("FileIO", "파일 쓰기 실패")
                        }

                    } else {
                        binding.chatResponse.text = "Error: ${response.errorBody()?.string()}"
                        Log.e("ChatGPT", "Error: ${response.errorBody()?.string()}")
                    }

                } // end runOnUiThread

            } // end onResponse

        }) // end call?

    } // end sendMessageToChatGPT


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