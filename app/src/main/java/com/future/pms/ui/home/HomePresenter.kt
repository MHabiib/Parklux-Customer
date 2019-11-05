package com.future.pms.ui.home

import com.future.pms.network.ApiServiceInterface
import com.future.pms.model.Post
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter : HomeContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: ApiServiceInterface = RetrofitClient.create()
    private lateinit var view: HomeContract.View

    override fun loadData() {
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

    override fun loadDataAll() {
    }

    override fun deleteItem(item: Post) {
        //api.deleteUser(item.id)
    }

    override fun subscribe() {}

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: HomeContract.View) {
        this.view = view
    }

}