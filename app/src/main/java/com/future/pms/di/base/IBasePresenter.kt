package com.future.pms.di.base

interface IBasePresenter<in V : BaseMVPView> {
    fun attachView(view: V)
    fun detachView()
}