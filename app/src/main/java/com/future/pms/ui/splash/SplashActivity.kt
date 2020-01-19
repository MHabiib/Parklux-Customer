package com.future.pms.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.future.pms.R
import com.future.pms.di.component.DaggerActivityComponent
import com.future.pms.di.module.ActivityModule
import com.future.pms.model.oauth.Token
import com.future.pms.ui.base.BaseActivity
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.main.MainActivity
import com.future.pms.ui.superadmin.mainsuperadmin.MainActivitySuperAdmin
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ROLE_CUSTOMER
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashContract {
  @Inject lateinit var presenter: SplashPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val settings = getSharedPreferences("prefs", 0)
    val firstRun = settings.getBoolean("firstRun", false)
    if (!firstRun) {
      setContentView(R.layout.activity_splash)
      injectDependency()
      presenter.attach(this)
      initView()
    } else {
      val a = Intent(this, Dispatchers.Main::class.java)
      startActivity(a)
    }
  }

  private fun initView() {
    presenter.isAuthenticated()
  }

  override fun onSuccess() {
    val intent: Intent = if (Gson().fromJson(
            this.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
                Constants.TOKEN, null), Token::class.java).role == ROLE_CUSTOMER) {
      Intent(this, MainActivity::class.java)
    } else {
      Intent(this, MainActivitySuperAdmin::class.java)
    }
    Handler().postDelayed({
      startActivity(intent)
      finish()
    }, 500)
  }

  override fun onLogin() {
    showLogin()
  }

  override fun isAuthenticated(): Context? {
    return applicationContext
  }

  private fun showLogin() {
    Handler().postDelayed({
      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
      finish()
    }, 500)
  }

  override fun onFailed(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    showLogin()
  }

  private fun injectDependency() {
    val activityComponent = DaggerActivityComponent.builder().activityModule(
        ActivityModule(this)).build()
    activityComponent.inject(this)
  }
}