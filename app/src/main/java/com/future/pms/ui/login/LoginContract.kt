package com.future.pms.ui.login

import com.future.pms.di.base.BaseMVPView
import com.future.pms.di.base.IBasePresenter

class LoginContract {
    interface LoginView : BaseMVPView {
        fun onSuccess()
        fun onFailed(e: String)
        fun onError(e: Throwable)
    }

    interface LoginPresenterI : IBasePresenter<LoginView> {
        fun login(email: String, password: String)
        fun cancel()
    }
}