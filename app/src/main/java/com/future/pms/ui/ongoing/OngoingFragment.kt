package com.future.pms.ui.ongoing

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.oauth.Token
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.future.pms.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_ongoing.*
import kotlinx.android.synthetic.main.fragment_ongoing.view.*
import javax.inject.Inject

class OngoingFragment : Fragment(), OngoingContract {
    @Inject
    lateinit var presenter: OngoingPresenter
    private lateinit var rootView: View
    private lateinit var ongoingParking: CustomerBooking

    fun newInstance(): OngoingFragment {
        return OngoingFragment()
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
        rootView = inflater.inflate(R.layout.fragment_ongoing, container, false)
        val directionLayout = rootView.findViewById(R.id.directions_layout) as ConstraintLayout
        directionLayout.setOnClickListener {
            val activity = activity as MainActivity?
            activity?.presenter?.showParkingDirection()
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
            context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
                Constants.TOKEN, null), Token::class.java).access_token
        presenter.loadOngoingBooking(accessToken)
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun showErrorMessage(error: String) {
        Log.e(Constants.ERROR, error)
    }

    override fun loadCustomerOngoingSuccess(ongoing: CustomerBooking) {
        rootView.ongoing_parking_layout.visibility = View.VISIBLE
        rootView.parking_zone_name.text = ongoing.parkingZoneName
        rootView.parking_slot.text = ongoing.slotName
        rootView.parking_time.text = Utils.convertLongToTimeShortMonth(ongoing.dateIn)
    }

    private fun injectDependency() {
        val homeComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()
        homeComponent.inject(this)
    }

    companion object {
        const val TAG: String = Constants.ONGOING_FRAGMENT
    }
}