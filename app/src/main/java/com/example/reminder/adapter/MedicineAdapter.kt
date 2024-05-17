
package com.example.reminder.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reminder.R
import com.example.reminder.data.MedicineReminderData
import com.example.reminder.Constants

class MedicineAdapter(
    private val context: Context,
    private val onClickItem: OnClickItem
) : RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {

    var arrMedicineReminder = ArrayList<MedicineReminderData>()

    class ViewHolder(itemView: View, private val onClickItem: OnClickItem) :
        RecyclerView.ViewHolder(itemView) {

        var medicineCheckLayout: LinearLayout = itemView.findViewById(R.id.medicine_check)
        var enableBtn: ImageView = itemView.findViewById(R.id.enable_btn)
        var disableBtn: ImageView = itemView.findViewById(R.id.disable_btn)
        var medicineImage: ImageView = itemView.findViewById(R.id.medicine_image)
        var medicineName: TextView = itemView.findViewById(R.id.medicine_name)
        var medicineQnty: TextView = itemView.findViewById(R.id.medicine_qnty)
        var medicineInstruction: TextView = itemView.findViewById(R.id.medicine_instruction)
        var stockSize: TextView = itemView.findViewById(R.id.stock_size)
        var time: TextView = itemView.findViewById(R.id.time)

        fun bind(info: MedicineReminderData) {
            medicineImage.setImageResource(Constants.getMedicineType()[info.medicineImage].image)
            medicineName.text = info.medicineName
            medicineQnty.text = info.medicineQnty
            medicineInstruction.text = info.medicineInstruction
            stockSize.text = info.stockSize.toString()
            time.text = info.medicineTime

            itemView.setOnLongClickListener {
                AlertDialog.Builder(itemView.context).apply {
                    setTitle("삭제")
                    setMessage("이 내용을 지우시겠습니까?")
                    setPositiveButton("YES") { _, _ ->
                        onClickItem.deleteRow(info, adapterPosition)
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    show()
                }
                true
            }

            medicineCheckLayout.setOnClickListener {
                if (!info.medicineCheck) {
                    val defaultMedicineName = info.medicineName
                    val stockSize = info.stockSize - 1
                    info.medicineCheck = true
                    onClickItem.updateMedicineCheck(info, adapterPosition)
                    onClickItem.updateMedicineStatus(defaultMedicineName, stockSize)
                }
            }

            if (info.medicineCheck) {
                enableBtn.visibility = View.VISIBLE
                disableBtn.visibility = View.GONE
            } else {
                disableBtn.visibility = View.VISIBLE
                enableBtn.visibility = View.GONE
            }
        }

        init {
            // Move listeners here if any
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.medicine_reminder_recyclerview_item, parent, false),
            onClickItem
        )
    }

    override fun getItemCount(): Int = arrMedicineReminder.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrMedicineReminder[position])
    }

    fun updateMedicineList(medicineReminder: List<MedicineReminderData>) {
        val diffCallback = MedicineDiffCallback(arrMedicineReminder, medicineReminder)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        arrMedicineReminder.clear()
        arrMedicineReminder.addAll(medicineReminder)
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnClickItem {
        fun deleteRow(medicineReminder: MedicineReminderData, position: Int)
        fun updateMedicineCheck(medicineReminder: MedicineReminderData, position: Int)
        fun updateMedicineStatus(medicineName: String, medicineStock: Int)
    }

    class MedicineDiffCallback(
        private val oldList: List<MedicineReminderData>,
        private val newList: List<MedicineReminderData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
