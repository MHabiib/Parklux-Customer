package com.future.pms.ui.main

import io.reactivex.disposables.CompositeDisposable

class MainPresenter : MainContract.Presenter {

    private val subscriptions = CompositeDisposable()

    private lateinit var view: MainContract.View
    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun onHomeIconClick() {
        view.showHomeFragment()
    }

    override fun onScanIconClick() {
        view.showScanFragment()
    }

    override fun onProfileIconClick() {
        view.showProfileFragment()
    }

    override fun attach(view: MainContract.View) {
        this.view = view
        view.showHomeFragment()
    }

}