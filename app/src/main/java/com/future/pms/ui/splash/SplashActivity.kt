package com.future.pms.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.future.pms.R
import com.future.pms.di.base.BaseMVPActivity
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseMVPActivity<SplashContract.SplashView,
        SplashContract.SplashPresenter>(), SplashContract.SplashView {
    override var presenter: SplashContract.SplashPresenter = SplashPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter.isAuthenticated()
    }

    override fun onSuccess() {
        val intent = Intent(this, MainActivity::class.java)
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
        Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        showLogin()
    }
}