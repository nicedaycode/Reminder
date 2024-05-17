
package com.example.reminder.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reminder.R
import com.example.reminder.model.Types

class MedicineListAdapter(
    private val context: Context,
    private val arrMedicineTypes: ArrayList<Types>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MedicineListAdapter.ViewHolder>() {

    private var selectedPosition: Int = -1 // 변경: 기본값을 null에서 -1로 변경하여, 선택된 항목이 없음을 명확히 표현

    class ViewHolder(itemView: View, onItemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var itemTxt: TextView = itemView.findViewById(R.id.item_txt)
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemBg: LinearLayout = itemView.findViewById(R.id.item_bg)

        init {
            itemView.setOnClickListener {
                onItemClickListener.updateBackground(adapterPosition)
            }
        }

        fun bind(info: Types, isSelected: Boolean) {
            itemTxt.text = info.name
            itemImage.setImageResource(info.image)
            itemBg.setBackgroundResource(if (isSelected) R.drawable.selected_medicine_bg else android.R.color.transparent) // 변경: 선택 상태에 따라 배경을 조건부로 설정
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.medicine_types, parent, false),
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isSelected = position == selectedPosition // 변경: 선택된 항목이면 true, 아니면 false
        val medicineType = arrMedicineTypes[position]
        holder.bind(medicineType, isSelected)
    }

    override fun getItemCount(): Int = arrMedicineTypes.size

    interface OnItemClickListener {
        fun updateBackground(position: Int)
    }

    fun changeUI(position: Int) {
        if (selectedPosition != position) { // 변경: 동일한 항목을 다시 선택하지 않도록 체크
            val prevSelectedPosition = selectedPosition
            selectedPosition = position

            if (prevSelectedPosition >= 0) { // 변경: 이전 선택된 항목이 유효한 범위 내에 있을 때만 업데이트
                notifyItemChanged(prevSelectedPosition)
            }
            notifyItemChanged(position)
        }
    }
}
