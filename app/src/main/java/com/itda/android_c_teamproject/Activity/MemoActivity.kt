package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.databinding.ActivityMemoBinding
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class MemoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.run {
                val fileName = "itda.txt"   // 저장할 파일 이름
            // 파일 존재 여부 확인
            val file = File(filesDir, fileName)
            if (!file.exists()) {
                Log.e("FileIO", "파일이 존재하지 않습니다: $fileName")
                textView.text = "파일이 존재하지 않습니다."
                return@run
            }
            try {

                val openFileInput = openFileInput(fileName)
                val inputStreamReader = InputStreamReader(openFileInput)    // 문자열 읽기
                val bufferedReader = BufferedReader(inputStreamReader)  // 버퍼 리더
                val sb = StringBuilder()        //  문자열 담을 문자열빌더
                var text: String?       // 한줄 읽을 문자열

                while (bufferedReader.readLine().also {
                        text = it
                    } != null) {
                    // 파일을 끝까지 한줄씩 읽고,
                    sb.append(text).append("\n") // 빌더에 추가
                }

                // 리소스 정리
                openFileInput.close()
                inputStreamReader.close()
                bufferedReader.close()

                // 빌더에 담은 문자열 반환
                val readDate = sb.toString()

                // 뷰에 표시
                textView.text = readDate
                Log.d("FileIO", "파일 읽기 성공: $fileName")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("FileIO", "파일 읽기 실패")
            }

            textExit.setOnClickListener {
                startActivity(Intent(this@MemoActivity, FirstActivity::class.java))
            }
        }
    }
}