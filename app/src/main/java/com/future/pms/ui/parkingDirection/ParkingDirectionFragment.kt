package com.future.pms.ui.parkingDirection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.pms.R
import com.future.pms.di.base.BaseMVPFragment

class ParkingDirectionFragment :
    BaseMVPFragment<ParkingDirectionContract.View, ParkingDirectionContract.Presenter>(),
    ParkingDirectionContract.View {

    override var presenter: ParkingDirectionContract.Presenter = ParkingDirectionPresenter()

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_parking_direction, container, false)

        return rootView
    }


    override fun onParkingSlotView(seats: String) {
        var seats = (
                "_UUUUUUAAAAARRRR____________________________/"
                        + "___________________UUUUUUAAAAAA/"
                        + "UU__AAAARRRRR__RR/"
                        + "UU__UUUAAAAAA__AA/"
                        + "AA__AAAAAAAAA__AA/"
                        + "AA__AARUUUURR__AA/"
                        + "UU__UUUA_RRRR__AA/"
                        + "AA__AAAA_RRAA__UU/"
                        + "AA__AARR_UUUU__RR/"
                        + "AA__UUAA_UURR__RR/"
                        + "_________________/"
                        + "UU_AAAAAAAUUUU_RR/"
                        + "RR_AAAAAAAAAAA_AA/"
                        + "AA_UUAAAAAUUUU_AA/"
                        + "AA_AAAAAAUUUUU_AA/"
                        + "_________________/")
    }

    override fun showProgress(show: Boolean) {}

}
