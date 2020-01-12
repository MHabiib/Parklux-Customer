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
import com.future.pms.ui.superadmin.mainsuperadmin.MainActivitySuperAdmin
import com.future.pms.util.Authentication
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ROLE_ADMIN
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), SplashContract {
  @Inject lateinit var presenter: SplashPresenter
  private val subscriptions = CompositeDisposable()

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

  override fun onResume() {
    super.onResume()
    val settings = getSharedPreferences("prefs", 0)
    val firstRun = settings.getBoolean("firstRun", false)
    if (!firstRun) {
      setContentView(R.layout.activity_splash)
      injectDependency()
      presenter.attach(this)
      initView()
    }
  }

  private fun initView() {
    presenter.isAuthenticated()
  }

  override fun onSuccess() {
    val intent: Intent = if (Gson().fromJson(
            this.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
                Constants.TOKEN, null), Token::class.java).role == ROLE_ADMIN) {
      Intent(this, MainActivity::class.java)
    } else {
      Intent(this, MainActivitySuperAdmin::class.java)
    }
    Handler().postDelayed({
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
    val subscribe = authFetcher.refresh(GRANT_TYPE,
        Authentication.getRefresh(applicationContext)).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ token: Token ->
      Authentication.save(applicationContext, token, token.role)
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
        ActivityModule(this)).build()
    activityComponent.inject(this)
  }
}