package com.future.pms.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.future.pms.R
import com.future.pms.di.base.BaseMVPActivity
import com.future.pms.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseMVPActivity<LoginContract.LoginView, LoginContract.LoginPresenterI>(),
    LoginContract.LoginView {
    override var presenter: LoginContract.LoginPresenterI = LoginPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnSign.setOnClickListener {
            if (isValid()) {
                loading(true)
                hideKeyboard()
                presenter.login(txtEmail.text.toString(), txtPassword.text.toString())
            }
        }
    }

    private fun isValid(): Boolean {
        if (txtEmail?.text.toString().isEmpty()) return false
        if (txtPassword?.text.toString().isNullOrEmpty()) return false
        return true
    }

    override fun onSuccess() {
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

    private fun loading(value: Boolean) {
        if (!value) {
            txtEmail.text?.clear()
            txtPassword.text?.clear()
        }
        if (value) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE

        if (!value) inputLayoutEmail.visibility = View.VISIBLE
        else inputLayoutEmail.visibility = View.GONE

        if (!value) inputLayoutPassword.visibility = View.VISIBLE
        else inputLayoutPassword.visibility = View.GONE

        if (!value) btnSign.visibility = View.VISIBLE
        else btnSign.visibility = View.GONE
    }

    override fun onFailed(e: String) {
        loading(false)
        Toast.makeText(this, e, Toast.LENGTH_LONG).show()
    }

    override fun onError(e: Throwable) {
        loading(false)
        Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
    }
}
