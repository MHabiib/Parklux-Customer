package com.future.pms.ui.login

import com.future.pms.di.base.BaseMVPPresenterImpl
import com.future.pms.R
import com.future.pms.model.oauth.Token
import com.future.pms.model.oauth.request.Auth
import com.future.pms.network.AuthFetcher
import com.future.pms.util.Authentication

class LoginPresenterImpl : BaseMVPPresenterImpl<LoginContract.LoginView>(),
    LoginContract.LoginPresenter {

    private var authFetcher: AuthFetcher.AuthFetcherImpl?= null

    override fun login(email: String, password: String) {
        authFetcher = AuthFetcher.AuthFetcherImpl(getContext(), object : AuthFetcher.Listener {
            override fun onSuccess(token: Token?) {
                if(token == null) {
                    view?.let { view -> call(view,
                        getContext().getString(R.string.auth_invalid),
                        view::onFailed)
                    }
                } else {
                    Authentication.save(getContext(), token)
                    view?.let { view -> call(view, view::onSuccess)}
                }
            }
            override fun onError(throwable: Throwable) {
                view?.let { view -> call(view, throwable, view::onError) }
            }
        })
        authFetcher?.auth(Auth(email, password))
    }

    override fun cancel() {
        authFetcher?.cancel()
    }
}