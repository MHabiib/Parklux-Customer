package com.future.pms.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.future.pms.R
import com.future.pms.di.component.DaggerActivityComponent
import com.future.pms.di.module.ActivityModule
import com.future.pms.model.oauth.Token
import com.future.pms.network.APICreator
import com.future.pms.network.AuthAPI
import com.future.pms.network.NetworkConstant.GRANT_TYPE
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), SplashContract {
  @Inject lateinit var presenter: SplashPresenter
  private val subscriptions = CompositeDisposable()

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
    Handler().postDelayed({
      val intent = Intent(this, MainActivity::class.java)
      startActivity(intent)
      finish()
    }, 1000)
  }

  override fun onLogin() {
    showLogin()
  }

  override fun isAuthenticated(): Context? {
    return applicationContext
  }

  override fun refreshFetcher() {
    val authFetcher = APICreator(AuthAPI::class.java).generate()
    val subscribe =
      authFetcher.refresh(GRANT_TYPE, Authentication.getRefresh(applicationContext)).subscribeOn(
        Schedulers.io()
      ).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
        Authentication.save(applicationContext, token)
        onSuccess()
      }, { onLogin() })
    subscriptions.add(subscribe)
  }

  private fun showLogin() {
    Handler().postDelayed({
      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
      finish()
    }, 1000)
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
      ActivityModule(this)
    ).build()
    activityComponent.inject(this)
  }
}