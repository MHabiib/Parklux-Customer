package com.future.pms.ui.parkingdirection

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.future.pms.R
import com.future.pms.di.base.BaseMVPPresenter
import com.future.pms.di.base.BaseMVPPresenterImpl
import com.future.pms.ui.home.HomeContract
import com.future.pms.util.Constants.Companion.STATUS_AVAILABLE
import com.future.pms.util.Constants.Companion.STATUS_BOOKED
import com.future.pms.util.Constants.Companion.STATUS_RESERVED
import com.future.pms.util.Constants.Companion.seatGaping
import com.future.pms.util.Constants.Companion.seatSize
import com.future.pms.util.Constants.Companion.selectedIds
import javax.inject.Inject

class ParkingDirectionPresenter @Inject constructor(){

    private lateinit var view: ParkingDirectionContract

    fun attach(view: ParkingDirectionContract) {
        this.view = view
    }
}