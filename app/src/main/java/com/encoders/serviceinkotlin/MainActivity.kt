package com.encoders.serviceinkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.encoders.serviceinkotlin.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var serviceintent: Intent
    private  var time = 0.0
    private var timerStarted =  false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        serviceintent = Intent(this , TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATE))

        binding.startstopbutton.setOnClickListener { startStopTimer() }
        binding.resetbutton.setOnClickListener { resetTimer() }
    }

    private  var updateTime: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
             time  = intent.getDoubleExtra(TimerService.TIME_EXTRA , 0.0)
            binding.timeTV.text = GetTimeStringFromDouble(time)

        }

    }

    private fun GetTimeStringFromDouble(time: Double): String{

        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minute = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return makeTimerString(hours, minute,seconds)

    }

    private fun makeTimerString(hour: Int , minute: Int, seconds: Int): String = String.format("%02d:%02d:%02d",hour,minute,seconds)


    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.timeTV.text =  GetTimeStringFromDouble(time)
    }

    private fun startStopTimer() {
         if (timerStarted) {
             stopTimer()
         }
        else {
             startTimer()
         }

    }

    private fun startTimer(){
        binding.startstopbutton.text = "Stop"
        timerStarted = true
        serviceintent.putExtra(TimerService.TIME_EXTRA,time)
        startService(serviceintent)


    }

    private fun stopTimer(){
        stopService(serviceintent)
        binding.startstopbutton.text = "Start"
        timerStarted = false
    }
}