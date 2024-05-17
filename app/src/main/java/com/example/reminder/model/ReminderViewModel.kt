package com.example.reminder.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminder.repository.MedicineRepository
import com.example.reminder.data.MedicineReminderData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderViewModel(private val medicineRepository: MedicineRepository) : ViewModel() {

    fun getReminder(): LiveData<List<MedicineReminderData>> {
        return medicineRepository.getReminder()
    }

    fun getDonateMedicine(): LiveData<Int> {
        return medicineRepository.getDonateMedicine()
    }

    fun getStockMedicine(): LiveData<List<Restock>> {
        return medicineRepository.getStockMedicine()
    }

    fun insertReminder(medicineReminder: MedicineReminderData) {
        viewModelScope.launch(Dispatchers.IO) {
            medicineRepository.insertReminder(medicineReminder)
        }
    }

    fun instertsReminder(medicineReminders: List<MedicineReminderData>) {
        viewModelScope.launch(Dispatchers.IO) {
            for(item in medicineReminders)
                medicineRepository.insertReminder(item)

        }
    }

    fun deleteReminder(medicineReminder: MedicineReminderData) {
        viewModelScope.launch(Dispatchers.IO) {
            medicineRepository.deleteReminder(medicineReminder)
        }
    }

    fun updateReminder(medicineReminder: MedicineReminderData) {
        viewModelScope.launch(Dispatchers.IO) {
            medicineRepository.updateReminder(medicineReminder)
        }
    }

    fun updateReminder(medicineName: String, medicineStock: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            medicineRepository.updateReminder(medicineName, medicineStock)
        }
    }
}