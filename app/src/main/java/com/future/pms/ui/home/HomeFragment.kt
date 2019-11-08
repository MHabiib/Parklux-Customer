package com.future.pms.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.DetailsViewModel
import com.future.pms.model.Post
import com.future.pms.ui.parkingdirection.ParkingDirectionFragment
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Constants.Companion.HOME_FRAGMENT
import kotlinx.android.synthetic.main.fragment_home.*
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
        val button = rootView.findViewById(R.id.button_go_to) as Button
        button.setOnClickListener{
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.add(R.id.container, ParkingDirectionFragment())
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
        presenter.loadData()
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

    override fun loadDataSuccess(list: List<Post>) {
        val adapter = HomeAdapter(activity, list.toMutableList())
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    override fun showErrorMessage(error: String) {
        Log.e(ERROR, error)
    }

    override fun loadDataAllSuccess(model: DetailsViewModel) {
        print(model.toJson())
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = activity?.supportFragmentManager
        val ft = manager?.beginTransaction()

        if (addToBackStack) {
            ft?.addToBackStack(tag)
        }
        ft?.replace(com.future.pms.R.id.frame, fragment, tag)
        ft?.commitAllowingStateLoss()
    }

    override fun showParkingDirectionFragment() {
    }

    private fun injectDependency() {
        val homeComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        homeComponent.inject(this)
    }

    companion object {
        const val TAG: String = HOME_FRAGMENT
    }
}