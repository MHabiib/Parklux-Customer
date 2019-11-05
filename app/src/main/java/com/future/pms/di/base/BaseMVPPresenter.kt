package com.future.pms.di.base

interface BaseMVPPresenter<in V : BaseMVPView> {
    fun attachView(view: V)
    fun detachView()
}