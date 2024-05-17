package com.example.reminder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.reminder.activity.SignIn
import com.example.reminder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()
        onBoardingFinished()
        val intent = Intent(this, SignIn::class.java)
        startActivity(intent)


    }
    private fun onBoardingFinished(): Boolean{
        val sharedPref = application.getSharedPreferences("login", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("flag",false)
    }
    companion object IntentMSG{
        const val INTENT_EXTRA_MSG_FREQUENCY = "schedulefrequency"
        const val INTENT_EXTRA_MSG_WEEKDAY = "scheduleweekday"
        const val INTENT_EXTRA_MSG_START = "durationstart"
        const val INTENT_EXTRA_MSG_END = "durationend"
        const val INTENT_EXTRA_MSG_TIME1 = "time1"
        const val INTENT_EXTRA_MSG_TIME2 = "time2"
        const val INTENT_EXTRA_MSG_TIME3 = "time3"
        const val INTENT_EXTRA_MSG_TIME4 = "time4"
        const val INTENT_EXTRA_MSG_TABLET1 = "tablet1"
        const val INTENT_EXTRA_MSG_TABLET2 = "tablet2"
        const val INTENT_EXTRA_MSG_TABLET3 = "tablet3"
        const val INTENT_EXTRA_MSG_TABLET4 = "tablet4"
        const val INTENT_EXTRA_MSG_ISSCHEDULE = "isschedule"
        const val INTENT_EXTRA_MSG_UNIT = "unit"
    }
}