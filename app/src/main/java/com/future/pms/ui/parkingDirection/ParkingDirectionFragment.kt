package com.future.pms.ui.parkingDirection

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.future.pms.R
import java.util.ArrayList
//https://github.com/varunjohn/Booking-Seats-Layout-Sample

class ParkingDirectionFragment : Fragment() {

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

    private var seatViewList: MutableList<TextView> = ArrayList()
    private var seatSize = 100
    private var seatGaping = 10

    private var STATUS_AVAILABLE = 1
    private var STATUS_BOOKED = 2
    private var STATUS_RESERVED = 3
    private var selectedIds = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var layout = findViewById<LinearLayout>(R.id.layoutSeat)

        seats = "/$seats"

        val layoutSeat = LinearLayout(this)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutSeat.orientation = LinearLayout.VERTICAL
        layoutSeat.layoutParams = params
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping)
        layout.addView(layoutSeat)

        var count = 0

        for (index in seats.indices) {
            if (seats[index] == '/') {
                layout = LinearLayout(this)
                layout.orientation = LinearLayout.HORIZONTAL
                layoutSeat.addView(layout)
            } else if (seats[index] == 'U') {
                count++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(com.future.pms.R.drawable.ic_seats_booked)
                view.setTextColor(Color.WHITE)
                view.tag = STATUS_BOOKED
                view.text = count.toString() + ""
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seats[index] == 'A') {
                count++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(com.future.pms.R.drawable.ic_seats_book)
                view.text = count.toString() + ""
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.BLACK)
                view.tag = STATUS_AVAILABLE
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seats[index] == 'R') {
                count++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(com.future.pms.R.drawable.ic_seats_reserved)
                view.text = count.toString() + ""
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.WHITE)
                view.tag = STATUS_RESERVED
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seats[index] == '_') {
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setBackgroundColor(Color.TRANSPARENT)
                view.text = ""
                layout!!.addView(view)
            }
        }
    }

    override fun onClick(view: View) {
        if (view.tag as Int == STATUS_AVAILABLE) {
            if (selectedIds.contains(view.id.toString() + ",")) {
                selectedIds = selectedIds.replace((+view.id).toString() + ",", "")
                view.setBackgroundResource(com.future.pms.R.drawable.ic_seats_book)
            } else {
                selectedIds = selectedIds + view.id + ","
                view.setBackgroundResource(com.future.pms.R.drawable.ic_seats_selected)
            }
        } else if (view.tag as Int == STATUS_BOOKED) {
            Toast.makeText(this, "Seat " + view.id + " is Booked", Toast.LENGTH_SHORT).show()
        } else if (view.tag as Int == STATUS_RESERVED) {
            Toast.makeText(this, "Seat " + view.id + " is Reserved", Toast.LENGTH_SHORT).show()
        }
    }
}
