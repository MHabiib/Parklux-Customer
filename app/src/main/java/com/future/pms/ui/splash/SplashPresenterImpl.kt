package com.future.pms.ui.splash

import com.future.pms.di.base.BaseMVPPresenterImpl
import com.future.pms.R
import com.future.pms.model.oauth.Token
import com.future.pms.model.oauth.request.Refresh
import com.future.pms.ui.login.RefreshFetcher
import com.future.pms.util.Authentication

class SplashPresenterImpl : BaseMVPPresenterImpl<SplashContract.SplashView>(),
    SplashContract.SplashPresenter {

    private var refreshFetcher: RefreshFetcher.RefreshFetcherImpl? = null

    override fun isAuthenticated() {
        try {
            if(Authentication.isAuthenticated(getContext())){
                view?.let { view -> call(view, view::onSuccess) }
            } else {
                view?.let { view -> call(view,
                        getContext().getString(R.string.authenticating),
                        view::onMessage)
                }
                refreshFetcher = RefreshFetcher.RefreshFetcherImpl(getContext(),
                        object : RefreshFetcher.Listener {

                            override fun onSuccess(token: Token?) {
                                if(token == null){
                                    view?.let { view -> call(view, view::onLogin) }
                                }else{
                                    Authentication.save(getContext(), token)
                                    view?.let { view -> call(view, view::onSuccess) }
                                }
                            }

                            override fun onError(throwable: Throwable) {
                                view?.let { view -> call(view, throwable, view::onError) }
                            }
                        })
                refreshFetcher?.refresh(Refresh(Authentication.getRefresh(getContext())))
            }
        } catch (e: Authentication.WithoutAuthenticatedException) {
            view?.let { view -> call(view, view::onLogin) }
        }
    }

    override fun cancel() {
        refreshFetcher?.cancel()
    }
}