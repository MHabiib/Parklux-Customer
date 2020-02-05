package com.future.pms.ongoing.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao interface CheckoutStepOneUrlDao {
  @Query("SELECT * from imageUrl") fun getAll(): List<CheckoutStepOneUrl>

  @Insert(onConflict = REPLACE) fun insert(checkoutStepOneUrl: CheckoutStepOneUrl)

  @Delete fun delete(checkoutStepOneUrl: CheckoutStepOneUrl)
}