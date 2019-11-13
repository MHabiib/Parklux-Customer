package com.future.pms.ui.history

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoryPresenter @Inject constructor() {
    private val api: ApiServiceInterface = RetrofitClient.create()
    private val subscriptions = CompositeDisposable()
    private lateinit var view: HistoryContract

    fun loadCustomerBooking(access_token: String) {
        val subscribe = api.getCustomerBooking(access_token).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<CustomerBooking> ->
                view.showProgress(false)
                view.loadCustomerBookingSuccess(list)
            }, { error ->
                view.showProgress(false)
                view.showErrorMessage(error.localizedMessage)
            })
        subscriptions.add(subscribe)
    }

    fun subscribe() {}

    fun unsubscribe() {
        subscriptions.clear()
    }

    fun attach(view: HistoryContract) {
        this.view = view
    }
}