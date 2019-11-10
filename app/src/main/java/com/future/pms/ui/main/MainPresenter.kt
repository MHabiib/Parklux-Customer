package com.future.pms.ui.main

import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainPresenter @Inject constructor() {

    private lateinit var view: MainContract
    private val subscriptions = CompositeDisposable()

    fun subscribe() {

    }

    fun unsubscribe() {
        subscriptions.clear()
    }

    fun onHomeIconClick() {
        view.showHomeFragment()
    }

    fun onScanIconClick() {
        view.showScanFragment()
    }

    fun onProfileIconClick() {
        view.showProfileFragment()
    }

    fun attach(view: MainContract) {
        this.view = view
        view.showHomeFragment()
    }

}