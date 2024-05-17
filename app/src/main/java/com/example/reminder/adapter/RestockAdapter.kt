

package com.example.reminder.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reminder.R
import com.example.reminder.model.Restock

class RestockAdapter(
    private val context: Context,
    private val onStockItemClick: OnStockItemClick
) :
    RecyclerView.Adapter<RestockAdapter.ViewHolder>() {

    private val arrRestockMedicine = ArrayList<Restock>()

    class ViewHolder(itemView: View, private val onStockItemClick: OnStockItemClick) :
        RecyclerView.ViewHolder(itemView) {
        var medicineName: TextView = itemView.findViewById(R.id.medicine_name)
        var medicineStock: TextView = itemView.findViewById(R.id.medicine_stock)

        fun bind(info: Restock) {
            medicineName.text = info.name
            medicineStock.text = info.number.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.restock_medicine_recyclerview_item, parent, false),
            onStockItemClick
        )
    }

    override fun getItemCount(): Int = arrRestockMedicine.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = arrRestockMedicine[position]
        holder.bind(pos)

        holder.itemView.setOnClickListener {
            // 다이얼로그 생성 및 초기화
            val dialog = Dialog(it.context).apply {
                setContentView(R.layout.stock_adjustment)
                findViewById<TextView>(R.id.medicine_name).text = pos.name
                val stockQty = findViewById<EditText>(R.id.stock_qty_edt)
                findViewById<Button>(R.id.set_stock).setOnClickListener {
                    //  pos.number = stockQty.text.toString().toIntOrNull() ?: pos.number // 변경: 사용자 입력을 바로 해당 항목에 반영
                    onStockItemClick.updateStockMedicine(pos.name, pos.number)
                    notifyItemChanged(position) // 변경: UI 갱신을 위한 호출
                    dismiss()
                }
            }
            dialog.show()
        }
    }


    fun updateStockList(restockMedicine: List<Restock>) {
        arrRestockMedicine.clear()
        arrRestockMedicine.addAll(restockMedicine)
        notifyDataSetChanged()
    }

    interface OnStockItemClick {
        fun updateStockMedicine(medicineName: String, medicineStock: Int)
    }
}
