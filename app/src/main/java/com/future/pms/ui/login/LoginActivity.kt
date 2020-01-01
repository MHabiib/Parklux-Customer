package com.future.pms.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.future.pms.R
import com.future.pms.di.component.DaggerActivityComponent
import com.future.pms.di.module.ActivityModule
import com.future.pms.model.oauth.Token
import com.future.pms.ui.main.MainActivity
import com.future.pms.ui.register.RegisterActivity
import com.future.pms.util.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginContract {
  @Inject lateinit var presenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    injectDependency()
    presenter.attach(this)
    presenter.subscribe()
    btnSign.setOnClickListener {
      if (isValid()) {
        loading(true)
        hideKeyboard()
        presenter.login(txtEmail.text.toString(), txtPassword.text.toString())
      }
    }
    register.setOnClickListener {
      val intent = Intent(this, RegisterActivity::class.java)
      startActivity(intent)
      finish()
    }
  }

  private fun isValid(): Boolean {
    if (txtEmail?.text.toString().isEmpty()) return false
    if (txtPassword?.text.toString().isNullOrEmpty()) return false
    return true
  }

  override fun onSuccess() {
    presenter.loadData(Gson().fromJson(
        this.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken)
  }

  override fun onAuthorized() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun hideKeyboard() {
    val view = currentFocus
    view?.let {
      val mInputMethodManager = getSystemService(
          Activity.INPUT_METHOD_SERVICE) as InputMethodManager
      mInputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
  }

  private fun loading(isLoading: Boolean) {
    if (isLoading) {
      inputLayoutEmail.visibility = View.GONE
      progressBar.visibility = View.VISIBLE
      inputLayoutPassword.visibility = View.GONE
      btnSign.visibility = View.GONE
      dont_have_account.visibility = View.GONE
      register.visibility = View.GONE
    } else {
      txtPassword.text?.clear()
      progressBar.visibility = View.GONE
      inputLayoutEmail.visibility = View.VISIBLE
      inputLayoutPassword.visibility = View.VISIBLE
      btnSign.visibility = View.VISIBLE
      dont_have_account.visibility = View.VISIBLE
      register.visibility = View.VISIBLE
    }
  }

  override fun onFailed(e: String) {
    loading(false)
    Toast.makeText(this, e, Toast.LENGTH_LONG).show()
  }

  override fun onError() {
    loading(false)
    Toast.makeText(this, getString(R.string.email_password_incorect), Toast.LENGTH_LONG).show()
  }

  override fun onBackPressed() {
    super.onBackPressed()
    this.finishAffinity()
  }

  private fun injectDependency() {
    val activityComponent = DaggerActivityComponent.builder().activityModule(
        ActivityModule(this)).build()
    activityComponent.inject(this)
  }
}
