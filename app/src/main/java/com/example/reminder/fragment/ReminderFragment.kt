package com.example.reminder.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reminder.activity.MedicineActivity
import com.example.reminder.R
import com.example.reminder.adapter.MedicineAdapter
import com.example.reminder.databinding.FragmentReminderBinding
import com.example.reminder.model.ReminderViewModel
import com.example.reminder.model.ReminderViewModelFactory
import com.example.reminder.repository.MedicineRepository
import com.example.reminder.data.MedicineReminderData
import com.example.reminder.data.MedicineReminderDatabase
import java.text.SimpleDateFormat
import java.util.*

class ReminderFragment : Fragment(), MedicineAdapter.OnClickItem {
    private lateinit var binding: FragmentReminderBinding
    private lateinit var adapter: MedicineAdapter
    private lateinit var reminderViewModel: ReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reminder, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmer.startShimmer()

        binding.addMedicineBtn.setOnClickListener {
            startActivity(Intent(it.context, MedicineActivity::class.java))
        }

        binding.medicineReminderRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        adapter = MedicineAdapter(requireContext(), this)
        binding.medicineReminderRecyclerview.adapter = adapter

        val dao = MedicineReminderDatabase.getDatabase(requireContext()).medicineReminderDao()
        val repository = MedicineRepository(dao)

        reminderViewModel = ViewModelProvider(this, ReminderViewModelFactory(repository)).get(ReminderViewModel::class.java)

        reminderViewModel.getReminder().observe(viewLifecycleOwner, { reminders ->
            Handler().postDelayed({
                binding.shimmer.stopShimmer()
                binding.shimmer.visibility = View.GONE
                binding.defaultLayout.visibility = if (reminders.isEmpty()) View.VISIBLE else View.GONE
                binding.medicineReminderRecyclerview.visibility = if (reminders.isEmpty()) View.GONE else View.VISIBLE
                adapter.updateMedicineList(reminders)
                binding.taskNo.text = adapter.itemCount.toString() + "개"
            }, 1000)
        })
    }

    override fun deleteRow(medicineReminder: MedicineReminderData, position: Int) {
        reminderViewModel.deleteReminder(medicineReminder)
        adapter.notifyItemRemoved(position)
    }

    override fun updateMedicineCheck(medicineReminder: MedicineReminderData, position: Int) {
        reminderViewModel.updateReminder(medicineReminder)
        adapter.notifyItemChanged(position)
    }

    override fun updateMedicineStatus(medicineName: String, medicineStock: Int) {
        reminderViewModel.updateReminder(medicineName, medicineStock)
        adapter.notifyDataSetChanged()
    }
}