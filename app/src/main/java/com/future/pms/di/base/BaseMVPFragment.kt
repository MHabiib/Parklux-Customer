package com.future.pms.di.base

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseMVPFragment<in V : BaseMVPView, P : BaseMVPPresenter<V>>
    : Fragment(), BaseMVPView {

    abstract var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)
    }

}