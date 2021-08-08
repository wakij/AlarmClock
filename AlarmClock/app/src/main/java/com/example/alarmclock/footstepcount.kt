package com.example.alarmclock

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class footstepcount {
    class MainActivity : AppCompatActivity() , CoroutineScope, SensorEventListener {
        private lateinit var mManager: SensorManager
        private lateinit var mSensor: Sensor

        private var isJogging = false
        private var steps = 0
        private var startSteps = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            mManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            mSensor = mManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

            val textView = findViewById<TextView>(R.id.textView)
            val button = findViewById<Button>(R.id.button)
            button.setOnClickListener {
                if (isJogging) {
                    finishJogging(it as Button, textView)
                } else {
                    startJogging(it as Button, textView)
                }
                isJogging = !isJogging
            }
        }

        private fun startJogging(button: Button, textView:TextView) {
            textView.text =""
            startSteps = this.steps
            button.text = resources.getText(R.string.finish)
        }

        private fun finishJogging(button: Button, textView:TextView) {
            button.text = resources.getText(R.string.now_sending)

            launch {
                val message = "${steps - startSteps}歩、歩きました！"
                val df = SimpleDateFormat("yyyy年MM月dd日HH持mm分", Locale.JAPAN)
                // ツイートする機能を入れる場所
                textView.text = message
                button.text = resources.getText(R.string.start)
            }
        }

        override fun onPause() {
            super.onPause()
            mManager.unregisterListener(this)
        }

        override fun onResume() {
            super.onResume()
            mManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        override fun onDestroy() {
            job.cancel()
            super.onDestroy()
        }

        override fun onSensorChanged(event: SensorEvent?) {
            steps++
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }







}