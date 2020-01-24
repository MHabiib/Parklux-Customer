package com.future.pms.splash.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.base.BaseActivity
import com.future.pms.core.model.Token
import com.future.pms.databinding.ActivitySplashBinding
import com.future.pms.login.view.LoginActivity
import com.future.pms.main.view.MainActivity
import com.future.pms.splash.injection.DaggerSplashComponent
import com.future.pms.splash.injection.SplashComponent
import com.future.pms.splash.presenter.SplashPresenter
import com.future.pms.superadmin.mainsuperadmin.view.MainActivitySuperAdmin
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ROLE_CUSTOMER
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashContract {
  private var daggerBuild: SplashComponent = DaggerSplashComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: SplashPresenter
  private lateinit var binding: ActivitySplashBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    val settings = getSharedPreferences("prefs", 0)
    val firstRun = settings.getBoolean("firstRun", false)
    if (!firstRun) {
      presenter.attach(this)
      if (isOnline()) {
        presenter.isAuthenticated()
      } else {
        binding.ibRefresh.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_LONG).show()
        binding.ibRefresh.setOnClickListener {
          if (isOnline()) {
            binding.ibRefresh.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            presenter.isAuthenticated()
          } else {
            Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_LONG).show()
          }
        }
      }
    } else {
      val a = Intent(this, Dispatchers.Main::class.java)
      startActivity(a)
    }
  }

  override fun onSuccess() {
    val intent: Intent = if (Gson().fromJson(
            this.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
                Constants.TOKEN, null), Token::class.java).role == ROLE_CUSTOMER) {
      Intent(this, MainActivity::class.java)
    } else {
      Intent(this, MainActivitySuperAdmin::class.java)
    }
    startActivity(intent)
    finish()
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

  private fun isOnline(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetwork = cm?.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
  }

  override fun onFailed(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    showLogin()
  }
}