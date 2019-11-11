package com.future.pms.ui.receipt

import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.receipt.Receipt
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import com.future.pms.ui.profile.ProfileContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReceiptPresenter @Inject constructor() {

    private val subscriptions = CompositeDisposable()
    private val api: ApiServiceInterface = RetrofitClient.create()
    private lateinit var view: ReceiptContract

    fun loadData(access_token: String, idBooking: String) {
        val subscribe = api.getBookingReceipt(idBooking, access_token).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ receipt: Receipt ->
                view.showProgress(false)
                view.loadReceiptSuccess(receipt)
            }, { error ->
                view.showProgress(false)
                view.showErrorMessage(error.localizedMessage)
            })
        subscriptions.add(subscribe)
    }

    fun attach(view: ReceiptContract) {
        this.view = view
    }

    fun subscribe() {}
}