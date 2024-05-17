package com.example.reminder

import com.example.reminder.model.Types
object Constants {
    fun getMedicineType(): ArrayList<Types> {
        val medicineList = ArrayList<Types>()
        val med1 = Types("기타",R.drawable.others)
        medicineList.add(med1)
        val med2 = Types("알약",R.drawable.tablet)
        medicineList.add(med2)
        val med3 = Types("시럽",R.drawable.syrup)
        medicineList.add(med3)
        val med4 = Types("연고",R.drawable.ointment)
        medicineList.add(med4)
        val med5 = Types("주사",R.drawable.injection)
        medicineList.add(med5)
        val med6 = Types("안약",R.drawable.dropper)
        medicineList.add(med6)
        val med7 = Types("진통제",R.drawable.homeopathy)
        medicineList.add(med7)
        return medicineList
    }

}