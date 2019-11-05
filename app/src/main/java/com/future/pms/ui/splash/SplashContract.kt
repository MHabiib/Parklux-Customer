package com.future.pms.ui.splash

import com.future.pms.di.base.BaseMVPPresenter
import com.future.pms.di.base.BaseMVPView

object SplashContract {

    interface SplashView : BaseMVPView {
        fun onSuccess()
        fun onMessage(e: String)
        fun onLogin()
        fun onError(e: Throwable)
    }

    interface SplashPresenter : BaseMVPPresenter<SplashView> {
        fun isAuthenticated()
        fun cancel()
    }
}