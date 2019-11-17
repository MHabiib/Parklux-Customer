package com.future.pms.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.future.pms.R
import com.future.pms.di.component.DaggerActivityComponent
import com.future.pms.di.module.ActivityModule
import com.future.pms.model.oauth.Token
import com.future.pms.network.RefreshFetcher
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Authentication
import com.future.pms.util.Constants
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), SplashContract {
  @Inject lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
      injectDependency()
      presenter.attach(this)
      initView()
    }

  private fun initView() {
    presenter.isAuthenticated()
  }

    override fun onSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLogin() {
        showLogin()
    }

  override fun isAuthenticated(): Context? {
    return applicationContext
  }

  override fun refreshFetcher() {
    val refreshFetcher = RefreshFetcher.RefreshFetcherImpl(applicationContext,
        object : RefreshFetcher.Listener {
          override fun onSuccess(token: Token?) {
            if (token == null) {
              onLogin()
            } else {
              Authentication.save(applicationContext, token)
              onSuccess()
            }
          }

          override fun onError(throwable: Throwable) {
            onError(throwable)
          }
        })
    refreshFetcher.refresh(Constants.REFRESH_TOKEN, Authentication.getRefresh(applicationContext))
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

  override fun onDestroy() {
    super.onDestroy()
    presenter.unsubscribe()
  }

  private fun injectDependency() {
    val activityComponent = DaggerActivityComponent.builder().activityModule(
        ActivityModule(this)).build()
    activityComponent.inject(this)
  }
}