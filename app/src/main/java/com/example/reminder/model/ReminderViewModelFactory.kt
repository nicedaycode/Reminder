package com.example.reminder.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reminder.repository.MedicineRepository

class ReminderViewModelFactory(private val medicineRepository: MedicineRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReminderViewModel(medicineRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}