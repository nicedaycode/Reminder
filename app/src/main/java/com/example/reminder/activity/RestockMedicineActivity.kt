package com.example.reminder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reminder.R
import com.example.reminder.adapter.RestockAdapter
import com.example.reminder.model.ReminderViewModel
import com.example.reminder.model.ReminderViewModelFactory
import com.example.reminder.repository.MedicineRepository
import com.example.reminder.data.MedicineReminderDatabase
import com.example.reminder.databinding.ActivityRestockBinding

class RestockMedicineActivity : AppCompatActivity(),RestockAdapter.OnStockItemClick {
    private lateinit var binding: ActivityRestockBinding
    private lateinit var restockMedicineViewModel: ReminderViewModel
    lateinit var adapter: RestockAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restock)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.restockMedicineRecyclerview.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = RestockAdapter(this,this)
        binding.restockMedicineRecyclerview.adapter = adapter

        val dao = MedicineReminderDatabase.getDatabase(this).medicineReminderDao()
        val repository = MedicineRepository(dao)

        restockMedicineViewModel = ViewModelProvider(
            this,
            ReminderViewModelFactory(repository)
        ).get(ReminderViewModel::class.java)

        restockMedicineViewModel.getStockMedicine().observe(this, Observer {
            Log.d("RAHUL",it.toString())
            adapter.updateStockList(it)
        })
    }

    override fun updateStockMedicine(medicineName: String, medicineStock: Int) {
        restockMedicineViewModel.updateReminder(medicineName,medicineStock)
        adapter.notifyDataSetChanged()
    }
}