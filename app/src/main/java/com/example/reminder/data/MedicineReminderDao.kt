package com.example.reminder.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.reminder.model.Restock

@Dao
interface MedicineReminderDao {
    @Query("SELECT * FROM medicine_reminder_details ")
    fun getMedicineReminder(): LiveData<List<MedicineReminderData>>

    @Query("UPDATE medicine_reminder_details SET Medicine_STOCK = :medicineStock WHERE Medicine_NAME = :medicineName")
    fun updateMedicineReminder(medicineName: String, medicineStock: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMedicineReminder(medicineReminder: MedicineReminderData)

    @Delete
    fun deleteMedicineReminder(medicineReminder: MedicineReminderData)

    @Update
    fun updateMedicineCheck(medicineReminder: MedicineReminderData)

    // Get Medicine Details
    @Query("SELECT SUM(Medicine_STOCK) FROM (SELECT DISTINCT Medicine_NAME ,Medicine_STOCK FROM medicine_reminder_details WHERE Medicine_TYPE = \"Tablet\")")
    fun getDonateMedicine() : LiveData<Int>

    // Get Stock Medicine Details
    @Query("SELECT DISTINCT Medicine_NAME ,Medicine_STOCK FROM medicine_reminder_details WHERE Medicine_TYPE = \"Tablet\" ")
    fun getStockMedicine() : LiveData<List<Restock>>


}