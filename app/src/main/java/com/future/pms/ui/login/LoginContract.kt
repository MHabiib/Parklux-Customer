package com.future.pms.ui.login

import com.future.pms.di.base.BaseMVPPresenter
import com.future.pms.di.base.BaseMVPView

class LoginContract {

    interface LoginView : BaseMVPView {
        fun onSuccess()
        fun onFailed(e: String)
        fun onError(e: Throwable)
    }

    interface LoginPresenter : BaseMVPPresenter<LoginView> {
        fun login(email: String, password: String)
        fun cancel()
    }
}