package com.itda.android_c_teamproject.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.itda.android_c_teamproject.databinding.ActivityPedometerBinding


private const val TAG = "PedometerActivity"

// 만보기 앱
class PedometerActivity : AppCompatActivity(), SensorEventListener {

    // SensorManager와 SensorEventListener를 사용하여 걸음 수 센서를 초기화하고 데이터 변경을 감지
    private lateinit var binding: ActivityPedometerBinding
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var steps = 0
    private var initialStepCount = 0
    private var isInitialStepCountSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedometerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // TextView를 처음에는 보이지 않도록 설정
        binding.textView.visibility = TextView.INVISIBLE

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


        // 활동 인식 권한이 부여 되었는지 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            // 권한이 없으면 권한 요청
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                1001
            )
        } else {
            // 권한이 이미 부여되어 있다면 startTracking() 메서드를 호출
            startTracking()
        }

        // 나가기 버튼 클릭 시 메인 화면으로 이동
        binding.exitButton.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 리셋 버튼 클릭 시 걸음수 0으로 초기화
        binding.resetButton.setOnClickListener {
            initialStepCount = steps + initialStepCount // 현재 걸음 수를 초기화 값에 더함
            steps = 0
            binding.textView.text = "걸음 수: 0"
        }
    } // end onCreate

    // 권한 요청 결과를 처리 하는 메서드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // 요청 코드가 1001이고, 권한이 부여 되었다면 startTracking()을 호출
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTracking()
        }
    }


    // startTracking 메서드에서 걸음 수 센서를 등록
    @SuppressLint("MissingPermission")
    private fun startTracking() {
        if (stepCounterSensor == null) {
            binding.textView.text = "No Step Counter Sensor!"
            binding.textView.visibility = TextView.VISIBLE
        } else {
            sensorManager.registerListener(
                this,
                stepCounterSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
            binding.textView.text = "Tracking started..."
            binding.textView.visibility = TextView.VISIBLE
            // 2초 후에 텍스트를 "걸음 수: 0"으로 변경
            Handler(Looper.getMainLooper()).postDelayed({
                binding.textView.text = "걸음 수: $steps"
                binding.textView.visibility = TextView.VISIBLE
            }, 2000)

        } // end else
    }


    // onSensorChanged 메서드에서 걸음 수를 업데이트하고 TextView에 반영
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            if (!isInitialStepCountSet) {
                initialStepCount = event.values[0].toInt()
                isInitialStepCountSet = true
            }
            steps = event.values[0].toInt() - initialStepCount
            binding.textView.text = "걸음 수: $steps"
        }
    }

    //  센서의 정확도 변경을 감지 하고, 로그 메시지를 출력
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                Log.d("SensorAccuracy", "High accuracy")
            }

            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                Log.d("SensorAccuracy", "Medium accuracy")
            }

            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                Log.d("SensorAccuracy", "Low accuracy")
            }

            SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                Log.d("SensorAccuracy", "Unreliable accuracy")
            }
            // High accuracy: 센서의 정확도가 높은 상태입니다. 이 상태에서는 데이터를 신뢰할 수 있습니다.
            // Medium accuracy: 센서의 정확도가 중간 정도입니다. 데이터의 신뢰도가 약간 떨어질 수 있습니다.
            // Low accuracy: 센서의 정확도가 낮은 상태입니다. 데이터의 신뢰도가 많이 떨어질 수 있습니다.
            // Unreliable accuracy: 센서의 정확도가 매우 낮거나 신뢰할 수 없는 상태입니다. 이 경우 데이터를 거의 신뢰할 수 없습니다.
        }
    }

    // onSaveInstanceState 및 onRestoreInstanceState 메서드를 사용하여 걸음 수 상태를 저장하고 복원하도록 유지
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("steps", steps)
        outState.putInt("initialStepCount", initialStepCount)
        outState.putBoolean("isInitialStepCountSet", isInitialStepCountSet)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        steps = savedInstanceState.getInt("steps")
        initialStepCount = savedInstanceState.getInt("initialStepCount")
        isInitialStepCountSet = savedInstanceState.getBoolean("isInitialStepCountSet")
        binding.textView.text = "걸음 수: $steps"
    }

    // onDestroy 메서드에서 센서 리스너를 해제
    override fun onDestroy() {
        super.onDestroy()
        // 액티비티가 파괴될 때 센서 리스너 등록을 해제합니다.
        sensorManager.unregisterListener(this)
    }
}