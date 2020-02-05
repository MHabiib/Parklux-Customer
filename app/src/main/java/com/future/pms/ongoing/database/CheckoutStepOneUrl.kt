package com.future.pms.ongoing.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize @Entity(tableName = "imageUrl") data class CheckoutStepOneUrl(
    @ColumnInfo(name = "imageUrl") var imageUrl: String, @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long = 0) : Parcelable