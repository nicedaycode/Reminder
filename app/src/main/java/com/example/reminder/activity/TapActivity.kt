//package com.example.reminder.activity
//
//import android.os.Bundle
//import android.view.View
//import android.view.animation.Animation
//import android.view.animation.ScaleAnimation
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.Fragment
//import com.example.reminder.R
//import com.example.reminder.databinding.ActivityTapBinding
//import com.example.reminder.fragment.SettingFragment
//import com.example.reminder.fragment.ReminderFragment
//import com.example.reminder.fragment.TipFragment
//
//class TapActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityTapBinding
//    private var selectedTab = Tab.REMINDER
//
//    private val scaleAnimation = ScaleAnimation(
//        0.8f,
//        1.0f,
//        1f,
//        1f,
//        Animation.RELATIVE_TO_SELF,
//        1.0f,
//        Animation.RELATIVE_TO_SELF,
//        0.0f
//    ).apply {
//        duration = 200
//        fillAfter = true
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_tap)
//
//        // 기본 프래그먼트 설정
//        replaceFragment(ReminderFragment())
//        setupTabListener()
//    }
//
//    private fun setupTabListener() {
//        binding.reminderLayout.setOnClickListener { selectTab(Tab.REMINDER) }
//        binding.tipLayout.setOnClickListener { selectTab(Tab.TIP) }
//        binding.dashboardLayout.setOnClickListener { selectTab(Tab.SETTINGS) }
//    }
//
//    private fun selectTab(tab: Tab) {
//        if (selectedTab != tab) {
//            when (tab) {
//                Tab.REMINDER -> {
//                    replaceFragment(ReminderFragment())
//                    binding.reminderLayout.startAnimation(scaleAnimation)
//                    updateTabVisuals(binding.reminderLayout, null, null, null)
//                }
//
//                Tab.TIP -> {
//                    replaceFragment(TipFragment())
//                    binding.tipLayout.startAnimation(scaleAnimation)
//                    updateTabVisuals(binding.tipLayout, binding.tipImg, binding.tipText, R.drawable.tips)
//                }
//
//                Tab.SETTINGS -> {
//                    replaceFragment(SettingFragment())
//                    binding.dashboardLayout.startAnimation(scaleAnimation)
//                    updateTabVisuals(binding.dashboardLayout, binding.dashboardImg, binding.dashboardText, R.drawable.dashboard)
//                }
//            }
//            selectedTab = tab
//        }
//    }
//
//    private fun updateTabVisuals(layout: LinearLayout, imageView: ImageView?, textView:TextView?, backImg: Int?) {
//        binding.apply {
//            tipLayout.background = null
//            tipImg.setBackgroundResource(R.drawable.tip_selected)
//            tipText.visibility = View.GONE
//            dashboardLayout.background = null
//            dashboardImg.setBackgroundResource(R.drawable.dashboard_selected)
//            dashboardText.visibility = View.GONE
//
//            when (layout) {
//                reminderLayout -> {
//                    reminderLayout.setBackgroundResource(R.drawable.nav_round_bg)
//                    reminderImgSelected.visibility = View.VISIBLE
//                    reminderImgDefault.visibility = View.GONE
//                    reminderText.visibility = View.VISIBLE
//                }
//
//                else -> {
//                    reminderLayout.background = null
//                    reminderImgDefault.visibility = View.VISIBLE
//                    reminderImgSelected.visibility = View.GONE
//                    reminderText.visibility = View.GONE
//
//                    layout.setBackgroundResource(R.drawable.nav_round_bg)
//                    imageView!!.setBackgroundResource(backImg!!)
//                    textView!!.visibility = View.VISIBLE
//                }
//            }
//        }
//    }
//
//    private fun replaceFragment(fragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.setReorderingAllowed(true)
//        transaction.replace(R.id.fragment_container, fragment)
//        transaction.commit()
//    }
//    override fun onBackPressed() {
//        //super.onBackPressed();
//    }
//
//    enum class Tab {
//        REMINDER, TIP, SETTINGS
//    }
//}

package com.example.reminder.activity

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.reminder.R
import com.example.reminder.databinding.ActivityTapBinding
import com.example.reminder.fragment.DonateFragment
import com.example.reminder.fragment.SettingFragment
import com.example.reminder.fragment.ReminderFragment
import com.example.reminder.fragment.TipFragment

class TapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTapBinding
    private var selectedTab = Tab.REMINDER

    private val scaleAnimation = ScaleAnimation(
        0.8f,
        1.0f,
        1f,
        1f,
        Animation.RELATIVE_TO_SELF,
        1.0f,
        Animation.RELATIVE_TO_SELF,
        0.0f
    ).apply {
        duration = 200
        fillAfter = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tap)

        // 기본 프래그먼트 설정
        replaceFragment(ReminderFragment())
        setupTabListener()
    }

    private fun setupTabListener() {
        binding.reminderLayout.setOnClickListener { selectTab(Tab.REMINDER) }
        binding.tipLayout.setOnClickListener { selectTab(Tab.TIP) }

        binding.sellLayout.setOnClickListener { selectTab(Tab.DONATE) }

        binding.dashboardLayout.setOnClickListener { selectTab(Tab.SETTINGS) }
    }

    private fun selectTab(tab: Tab) {
        if (selectedTab != tab) {
            when (tab) {
                Tab.REMINDER -> {
                    replaceFragment(ReminderFragment())
                    binding.reminderLayout.startAnimation(scaleAnimation)
                    updateTabVisuals(binding.reminderLayout, null, null, null)
                }

                Tab.TIP -> {
                    replaceFragment(TipFragment())
                    binding.tipLayout.startAnimation(scaleAnimation)
                    updateTabVisuals(binding.tipLayout, binding.tipImg, binding.tipText, R.drawable.tips)
                }

                Tab.DONATE -> {
                    replaceFragment(DonateFragment())
                    binding.sellLayout.startAnimation(scaleAnimation)
                    updateTabVisuals(binding.sellLayout, binding.sellImg, binding.sellText, R.drawable.donate_selected)
                }

                Tab.SETTINGS -> {
                    replaceFragment(SettingFragment())
                    binding.dashboardLayout.startAnimation(scaleAnimation)
                    updateTabVisuals(binding.dashboardLayout, binding.dashboardImg, binding.dashboardText, R.drawable.dashboard)
                }
            }
            selectedTab = tab
        }
    }

    private fun updateTabVisuals(layout: LinearLayout, imageView: ImageView?, textView:TextView?, backImg: Int?) {
        binding.apply {
            tipLayout.background = null
            tipImg.setBackgroundResource(R.drawable.tip_selected)
            tipText.visibility = View.GONE

            sellLayout.background = null
            sellImg.setBackgroundResource(R.drawable.donate_default)
            sellText.visibility = View.GONE


            dashboardLayout.background = null
            dashboardImg.setBackgroundResource(R.drawable.dashboard_selected)
            dashboardText.visibility = View.GONE

            when (layout) {
                reminderLayout -> {
                    reminderLayout.setBackgroundResource(R.drawable.nav_round_bg)
                    reminderImgSelected.visibility = View.VISIBLE
                    reminderImgDefault.visibility = View.GONE
                    reminderText.visibility = View.VISIBLE
                }

                else -> {
                    reminderLayout.background = null
                    reminderImgDefault.visibility = View.VISIBLE
                    reminderImgSelected.visibility = View.GONE
                    reminderText.visibility = View.GONE

                    layout.setBackgroundResource(R.drawable.nav_round_bg)
                    imageView!!.setBackgroundResource(backImg!!)
                    textView!!.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
    override fun onBackPressed() {
        //super.onBackPressed();
    }

    enum class Tab {
        REMINDER, TIP, SETTINGS, DONATE
    }
}

