package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityStopWatchBinding

class StopWatchActivity : AppCompatActivity() {

    var initTime = 0L        // 초기 시간
    var pauseTime = 0L       // 멈춘 시각

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.run {
            btnStart.setOnClickListener {
                chronometer.base = SystemClock.elapsedRealtime() + pauseTime
                chronometer.start()
                // 버튼 표시여부를 조정
                btnStart.isEnabled = false
                btnStop.isEnabled = true
                btnReset.isEnabled = true
            }

            btnStop.setOnClickListener {
                pauseTime = chronometer.base - SystemClock.elapsedRealtime()
                chronometer.stop()

                btnStart.isEnabled = true
                btnStop.isEnabled = false
                btnReset.isEnabled = true
            }

            btnReset.setOnClickListener {
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.stop()
                pauseTime = 0L

                btnStart.isEnabled = true
                btnStop.isEnabled = false
                btnReset.isEnabled = false
            }
            buttonExit.setOnClickListener {
                startActivity(Intent(this@StopWatchActivity, FirstActivity::class.java))
            }
        }

    }

    // 뒤로가기 버튼 이벤트 핸들러
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