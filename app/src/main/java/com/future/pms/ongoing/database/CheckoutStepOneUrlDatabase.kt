package com.future.pms.ongoing.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CheckoutStepOneUrl::class], version = 1)
abstract class CheckoutStepOneUrlDatabase : RoomDatabase() {

  abstract fun checkoutStepOneDao(): CheckoutStepOneUrlDao

  companion object {
    private var INSTANCE: CheckoutStepOneUrlDatabase? = null

    fun getInstance(context: Context): CheckoutStepOneUrlDatabase? {
      if (INSTANCE == null) {
        synchronized(CheckoutStepOneUrlDatabase::class) {
          INSTANCE = Room.databaseBuilder(context.applicationContext,
              CheckoutStepOneUrlDatabase::class.java, "checkoutStepOne.db").build()
        }
      }
      return INSTANCE
    }

    fun destroyInstance() {
      INSTANCE = null
    }
  }
}