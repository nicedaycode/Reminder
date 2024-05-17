package com.example.reminder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.reminder.databinding.StockUnitBinding

interface UnitDialogInterface {
    fun onClickDialogSetButton(qty: Int)
    fun isEmptyDialogQtyText()
}

class UnitDialog(
    unitDialogInterface: UnitDialogInterface,
) : DialogFragment() {
    private var _binding: StockUnitBinding? = null
    private val binding get() = _binding!!

    private var unitDialogInterface: UnitDialogInterface? = null

    init {
        this.unitDialogInterface = unitDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StockUnitBinding.inflate(inflater, container, false)
        binding.setQty.setOnClickListener {
            if (binding.stockQty.text.isEmpty()) {
                this.unitDialogInterface?.isEmptyDialogQtyText()
            }
            else {
                val qty = binding.stockQty.text.toString().toInt()
                this.unitDialogInterface?.onClickDialogSetButton(qty)
                dialog?.dismiss()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}