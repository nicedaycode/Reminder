package com.example.reminder.repository

import androidx.lifecycle.LiveData
import com.example.reminder.model.Restock
import com.example.reminder.data.MedicineReminderData
import com.example.reminder.data.MedicineReminderDao

class MedicineRepository(private val medicineReminderDao: MedicineReminderDao) {

    fun getReminder(): LiveData<List<MedicineReminderData>> {
        return medicineReminderDao.getMedicineReminder()
    }
    fun getDonateMedicine(): LiveData<Int> {
        return medicineReminderDao.getDonateMedicine()
    }
    fun getStockMedicine(): LiveData<List<Restock>> {
        return medicineReminderDao.getStockMedicine()
    }

    fun insertReminder(medicineReminder: MedicineReminderData) {
        medicineReminderDao.insertMedicineReminder(medicineReminder)
    }

    fun deleteReminder(medicineReminder: MedicineReminderData) {
        medicineReminderDao.deleteMedicineReminder(medicineReminder)
    }
    fun updateReminder(medicineReminder: MedicineReminderData) {
        medicineReminderDao.updateMedicineCheck(medicineReminder)
    }

    fun updateReminder(medicineName: String, medicineStock: Int) {
        medicineReminderDao.updateMedicineReminder(medicineName, medicineStock)
    }
}
