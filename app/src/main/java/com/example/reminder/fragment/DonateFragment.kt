//package com.example.reminder.fragment
//
//import android.animation.ValueAnimator
//import android.content.Intent
//import android.graphics.Typeface
//import android.net.Uri
//import android.os.Bundle
//import android.text.SpannableString
//import android.text.Spanned
//import android.text.style.StyleSpan
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.reminder.R
//import com.example.reminder.databinding.FragmentDonateBinding
//import com.example.reminder.model.ReminderViewModel
//import com.example.reminder.model.ReminderViewModelFactory
//import com.example.reminder.repository.MedicineRepository
//import com.example.reminder.data.MedicineReminderDatabase
//
//
//class DonateFragment : Fragment() {
//    private lateinit var binding: FragmentDonateBinding
//    private lateinit var donateViewModel : ReminderViewModel
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_donate, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        /*super.onViewCreated(view, savedInstanceState)
//
//        val dao = MedicineReminderDatabase.getDatabase(requireContext()).medicineReminderDao()
//        val repository = MedicineRepository(dao)
//
//        donateViewModel = ViewModelProvider(requireActivity(), ReminderViewModelFactory(repository)).get(ReminderViewModel::class.java)
//
//        var donateMedicine : Int
//        donateViewModel.getDonateMedicine().observe(requireActivity(), androidx.lifecycle.Observer {
//            if(it != null) {
//                donateMedicine = it
//                val anim = ValueAnimator.ofInt(0, donateMedicine)
//
//                anim.addUpdateListener {
//                    binding.medicineStockTxt.text = anim.animatedValue.toString()
//                }
//                anim.duration = 1500;
//                anim.start();
//            }else{
//                binding.medicineStockTxt.text = "0"
////                Toast.makeText(requireContext(),"Please Add Medicine",Toast.LENGTH_SHORT).show()
//            }
//        })
//
//
//        val text =
//            "The Best Way is - You can return the unwanted Medicines to the Pharmacy or Donate it to a Nearby NGO. (Kindly Read the Terms & Conditions)"
//        val ss = SpannableString(text)
//        val boldSpan = StyleSpan(Typeface.BOLD)
//        val boldSpan1 = StyleSpan(Typeface.BOLD)
//        ss.setSpan(boldSpan, 26, 55, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        ss.setSpan(boldSpan1, 75, 81, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.donateTxt.text = ss
//
//        binding.medicineDonateLink.setOnClickListener {
//            val openURL = Intent(Intent.ACTION_VIEW)
//            openURL.data = Uri.parse("https://www.google.com/search?rlz=1C1CHBF_enIN1025IN1025&sxsrf=APwXEderQZ2DJLuJ-gxfQfn_8asx7PvRvw:1685358772615&q=near+by+government+hospital+engios&sa=X&ved=2ahUKEwjPsdfXspr_AhXoTWwGHR6_CV8Q7xYoAHoECAYQAQ&biw=1536&bih=763&dpr=1.25")
//            startActivity(openURL)
//        }*/
//
//
//    }
//}

package com.example.reminder.fragment

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.reminder.R
import com.example.reminder.databinding.FragmentDonateBinding
import com.example.reminder.model.ReminderViewModel
import com.example.reminder.model.ReminderViewModelFactory
import com.example.reminder.repository.MedicineRepository
import com.example.reminder.data.MedicineReminderDatabase


class DonateFragment : Fragment() {
    private lateinit var binding: FragmentDonateBinding
    private lateinit var donateViewModel : ReminderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_donate, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = MedicineReminderDatabase.getDatabase(requireContext()).medicineReminderDao()
        val repository = MedicineRepository(dao)

        donateViewModel = ViewModelProvider(requireActivity(), ReminderViewModelFactory(repository)).get(ReminderViewModel::class.java)

        var donateMedicine : Int
        donateViewModel.getDonateMedicine().observe(requireActivity(), androidx.lifecycle.Observer {
            if(it != null) {
                donateMedicine = it
                val anim = ValueAnimator.ofInt(0, donateMedicine)

                anim.addUpdateListener {
                    //binding.medicineStockTxt.text = anim.animatedValue.toString()
                }
                anim.duration = 1500;
                anim.start();
            }else{
                //binding.medicineStockTxt.text = "0"
//                Toast.makeText(requireContext(),"Please Add Medicine",Toast.LENGTH_SHORT).show()
            }
        })


//        val text =
//            "The Best Way is - You can return the unwanted Medicines to the Pharmacy or Donate it to a Nearby NGO. (Kindly Read the Terms & Conditions)"
//        val ss = SpannableString(text)
//        val boldSpan = StyleSpan(Typeface.BOLD)
//        val boldSpan1 = StyleSpan(Typeface.BOLD)
//        ss.setSpan(boldSpan, 26, 55, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        ss.setSpan(boldSpan1, 75, 81, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        binding.donateTxt.text = ss

        binding.medicineDonateLink.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://ad.goodneighbors.kr/girl24_b/web/gsa/index.html?utm_source=%EA%B5%AC%EA%B8%80PC&utm_medium=cpc&utm_campaign=girl24_b&utm_content=A1&utm_term=%EA%B5%BF%EB%84%A4%EC%9D%B4%EB%B2%84%EC%8A%A4&gad_source=1")
            startActivity(openURL)
        }


    }
}