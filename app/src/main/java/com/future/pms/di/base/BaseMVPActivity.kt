package com.future.pms.di.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseMVPActivity<in V : BaseMVPView, P : BaseMVPPresenter<V>>
    : AppCompatActivity(), BaseMVPView {

    abstract var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}