package com.future.pms.ui.home

import com.future.pms.network.ApiServiceInterface
import com.future.pms.model.Post
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenter @Inject constructor() {

    private val subscriptions = CompositeDisposable()
    private val api: ApiServiceInterface = RetrofitClient.create()
    private lateinit var view: HomeContract

    fun loadData() {
        val subscribe = api.getPostList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list: List<Post>? ->
                view.showProgress(false)
                view.loadDataSuccess(list!!.take(10))
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

    fun attach(view: HomeContract) {
        this.view = view
    }

    fun onParkingDirectionClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}