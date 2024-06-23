package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityCounterBinding

class CounterActivity : AppCompatActivity() {
    lateinit var binding: ActivityCounterBinding
    var count = 0
    var initTime = 0L
    // 액티비티가 생성될 때 savedInstanceState가 매개변수로 전달됨
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            mainCount.setOnClickListener {
                count++
                textView.text = "$count"
            }
            buttonExit.setOnClickListener {
                startActivity(Intent(this@CounterActivity, FirstActivity::class.java))
            }
        }
    }

    // 액티비티가 일시적으로 소멸될 때
    // Bundle 객체에 임시 상태 데이터를 지정할 수 있음.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 번들 객체에 필요한 데이터를 저장 "Key", "Value"
        outState.putInt("count", count)
    }

    // 액티비티가 재생성 될때 번들 객체 savedInstanceState 통해 복원 가능
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val saveData: Int = savedInstanceState.getInt("count")
        count = saveData
        binding.textView.text = "$saveData"
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