package com.future.pms.ui.splash

import com.future.pms.di.base.BaseMVPView
import com.future.pms.di.base.IBasePresenter

object SplashContract {
    interface SplashView : BaseMVPView {
        fun onSuccess()
        fun onMessage(e: String)
        fun onLogin()
        fun onError(e: Throwable)
    }

    interface SplashPresenterI : IBasePresenter<SplashView> {
        fun isAuthenticated()
        fun cancel()
    }
}