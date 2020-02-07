package com.future.pms.register.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.base.BaseActivity
import com.future.pms.core.model.Token
import com.future.pms.core.network.Authentication
import com.future.pms.login.view.LoginActivity
import com.future.pms.main.view.MainActivity
import com.future.pms.register.injection.DaggerRegisterComponent
import com.future.pms.register.injection.RegisterComponent
import com.future.pms.register.presenter.RegisterPresenter
import com.future.pms.util.Constants
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity : BaseActivity(), RegisterContract {
  private var daggerBuild: RegisterComponent = DaggerRegisterComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: RegisterPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    presenter.attach(this)
    presenter.subscribe()
    btnRegister.setOnClickListener {
      if (isValid()) {
        loading(true)
        hideKeyboard()
        presenter.register(txtName.text.toString(), txtEmail.text.toString(),
            txtPassword.text.toString(), txtPhone.text.toString())
      } else {
        Toast.makeText(this, getString(R.string.fill_all_entries), Toast.LENGTH_LONG).show()
      }
    }

    login.setOnClickListener {
      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
      finish()
    }
  }

  private fun isValid(): Boolean {
    if (txtName?.text.toString().isEmpty()) return false
    if (!txtEmail?.text.toString().isEmailValid()) return false
    if (txtPassword?.text.toString().isEmpty()) return false
    if (txtPhone?.text.toString().isEmpty()) return false
    if (txtPassword?.text.toString() != txtReTypePassword?.text.toString()) return false
    return true
  }

  private fun String.isEmailValid(): Boolean = !TextUtils.isEmpty(
      this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

  private fun loading(isLoading: Boolean) {
    if (isLoading) {
      progressBarReg.visibility = View.VISIBLE
      register_layout.visibility = View.GONE
    } else {
      progressBarReg.visibility = View.GONE
      register_layout.visibility = View.VISIBLE
    }
  }

  override fun onSuccess(token: Token) {
    Authentication.save(this, token, Constants.ROLE_CUSTOMER)
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }

  override fun onFailed(message: String) {
    loading(false)
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }

  private fun hideKeyboard() {
    val view = currentFocus
    view?.let {
      val mInputMethodManager = getSystemService(
          Activity.INPUT_METHOD_SERVICE) as InputMethodManager
      mInputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
  }

  override fun onBackPressed() {
    super.onBackPressed()
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
  }

  override fun onDestroy() {
    presenter.detach()
    super.onDestroy()
  }
}