package com.future.pms.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.oauth.Token
import com.future.pms.ui.parkingdirection.ParkingDirectionFragment
import com.future.pms.ui.receipt.ReceiptFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Constants.Companion.HOME_FRAGMENT
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment(), HomeContract {


    @Inject
    lateinit var presenter: HomePresenter

    private lateinit var rootView: View


    fun newInstance(): HomeFragment {
        return HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        val button = rootView.findViewById(R.id.ongoing_parking_layout) as ConstraintLayout
        button.setOnClickListener {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.add(
                R.id.container,
                ParkingDirectionFragment().newInstance(),
                ParkingDirectionFragment.TAG
            )
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        initView()
    }

    private fun initView() {
        val accessToken = Gson().fromJson(
            context?.getSharedPreferences(
                Constants.AUTHENTCATION,
                Context.MODE_PRIVATE
            )?.getString(Constants.TOKEN, null), Token::class.java
        ).access_token
        getDateNow()
        presenter.loadData(accessToken)
        presenter.loadCustomerBooking(accessToken)
        val textAnnounce = rootView.findViewById(R.id.text_announce_user) as TextView
        textAnnounce.text = presenter.getTextAnnounce()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun loadCustomerBookingSuccess(list: List<CustomerBooking>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter =
            HomeAdapter(list) { booking: CustomerBooking -> customerBookingClick(booking) }
    }

    private fun customerBookingClick(booking: CustomerBooking) {
        idBooking = booking.idBooking
        fragmentManager!!.beginTransaction()
            .disallowAddToBackStack()
            .replace(R.id.frame, ReceiptFragment().newInstance(), ReceiptFragment.TAG)
            .commit()
        // Launch second activity, pass part ID as string parameter
        /* val showDetailActivityIntent = Intent(this, PartDetailActivity::class.java)
         showDetailActivityIntent.putExtra(Intent.EXTRA_TEXT, partItem.id.toString())
         startActivity(showDetailActivityIntent)*/
    }

    override fun loadCustomerDetailSuccess(customer: Customer) {
        rootView.user_name.text = customer.body.name
    }

    override fun showErrorMessage(error: String) {
        Log.e(ERROR, error)
    }

    override fun showParkingDirectionFragment() {
    }

    override fun getDateNow() {
        val currentDateTimeString = DateFormat.getDateInstance(DateFormat.FULL).format(Date())
        val dateText = rootView.findViewById(R.id.date_now) as TextView
        dateText.text = String.format("It's %s", currentDateTimeString)
    }

    private fun injectDependency() {
        val homeComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        homeComponent.inject(this)
    }

    companion object {
        const val TAG: String = HOME_FRAGMENT
        lateinit var idBooking: String
    }
}