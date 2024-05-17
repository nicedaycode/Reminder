package com.example.reminder.model

import androidx.room.ColumnInfo

data class Restock (
    @ColumnInfo(name = "Medicine_NAME")
    val name : String,
    @ColumnInfo(name = "Medicine_STOCK")
    val number : Int
)