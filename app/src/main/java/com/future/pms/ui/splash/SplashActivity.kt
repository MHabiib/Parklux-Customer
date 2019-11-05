package com.future.pms.ui.splash

import android.content.Intent
import android.os.Bundle
import com.future.pms.R
import com.future.pms.di.base.BaseMVPActivity
import com.future.pms.ui.home2.HomeActivity
import com.future.pms.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity: BaseMVPActivity<SplashContract.SplashView,
        SplashContract.SplashPresenter>(), SplashContract.SplashView {

    override var presenter: SplashContract.SplashPresenter = SplashPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        presenter.isAuthenticated()
    }

    override fun onSuccess() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onMessage(e: String) {
        txtMessage.text = e
    }

    override fun onLogin() {
        showLogin()
    }

    private fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onError(e: Throwable) {
        Snackbar.make(container, e.message.toString(), Snackbar.LENGTH_LONG)
        showLogin()
    }
}