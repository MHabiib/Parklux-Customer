package com.future.pms.ui.ongoing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.ui.main.MainActivity
import com.future.pms.ui.receipt.ReceiptFragment
import com.future.pms.ui.receipt.ReceiptPresenter
import com.future.pms.util.Constants
import javax.inject.Inject

class OngoingFragment : Fragment(), OngoingContract {
    @Inject
    lateinit var presenter: OngoingPresenter
    private lateinit var rootView: View

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