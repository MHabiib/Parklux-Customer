package com.future.pms.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.future.pms.R
import com.future.pms.di.component.DaggerActivityComponent
import com.future.pms.di.module.ActivityModule
import com.future.pms.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import javax.inject.Inject

class RegisterActivity : AppCompatActivity(), RegisterContract {
  @Inject lateinit var presenter: RegisterPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    injectDependency()
    presenter.attach(this)
    presenter.subscribe()
    btnRegister.setOnClickListener {
      if (isValid()) {
        loading(true)
        hideKeyboard()
        presenter.register(txtEmail.text.toString(), txtPassword.text.toString())
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
    if (txtEmail?.text.toString().isNullOrEmpty()) return false
    if (txtPassword?.text.toString().isNullOrEmpty()) return false
    if (txtPhone?.text.toString().isNullOrEmpty()) return false
    return true
  }

  private fun loading(value: Boolean) {
    if (!value) {
      progressBarReg.visibility = View.GONE
      txtName.text?.clear()
      txtEmail.text?.clear()
      txtPassword.text?.clear()
      txtPhone.text?.clear()
      register_layout.visibility = View.GONE
    } else {
      progressBarReg.visibility = View.VISIBLE
      register_layout.visibility = View.VISIBLE
    }
  }

  override fun onSuccess() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onFailed(e: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onError(e: Throwable) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun hideKeyboard() {
    val view = currentFocus
    view?.let {
      val mInputMethodManager = getSystemService(
        Activity.INPUT_METHOD_SERVICE
      ) as InputMethodManager
      mInputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
  }

  override fun onBackPressed() {
    super.onBackPressed()
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
  }

  private fun injectDependency() {
    val activityComponent = DaggerActivityComponent.builder().activityModule(
      ActivityModule(this)
    ).build()
    activityComponent.inject(this)
  }
}