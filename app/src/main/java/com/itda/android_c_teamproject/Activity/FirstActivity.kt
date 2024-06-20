package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.R

class FirstActivity : AppCompatActivity() {
    private lateinit var mainImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        mainImage = findViewById(R.id.mainImage)

        // 가로 모드인지 확인하고 이미지의 가시성을 설정합니다
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mainImage.visibility = ImageView.GONE
        } else {
            mainImage.visibility = ImageView.VISIBLE
        }

        val recommendExerciseButton: Button = findViewById(R.id.recommendExerciseButton)

        val foodMenuButton: Button = findViewById(R.id.foodMenuButton)

        val stopwatchButton: Button = findViewById(R.id.stopwatchButton)

        val gptButton: Button = findViewById(R.id.gptButton)


        recommendExerciseButton.setOnClickListener{
            val intent = Intent(this, ChatMainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "추천 운동 버튼 클릭됨", Toast.LENGTH_SHORT).show()
        }

        foodMenuButton.setOnClickListener{
            Toast.makeText(this, "식단 버튼 클릭됨", Toast.LENGTH_SHORT).show()
        }

        stopwatchButton.setOnClickListener{
            Toast.makeText(this, "스탑워치 버튼 클릭됨", Toast.LENGTH_SHORT).show()
        }

        gptButton.setOnClickListener{
            Toast.makeText(this, "GPT 버튼 클릭됨", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // 가로/세로 모드 변경 시 이미지의 가시성을 설정합니다
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mainImage.visibility = ImageView.GONE
        } else {
            mainImage.visibility = ImageView.VISIBLE
        }
    }

}