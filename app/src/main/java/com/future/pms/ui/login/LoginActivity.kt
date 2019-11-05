package com.future.pms.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.future.pms.R
import com.future.pms.di.base.BaseMVPActivity
import com.future.pms.ui.home2.HomeActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.progressBar
import kotlinx.android.synthetic.main.activity_splash.*

class LoginActivity : BaseMVPActivity<LoginContract.LoginView, LoginContract.LoginPresenter>(),
    LoginContract.LoginView {

    override var presenter: LoginContract.LoginPresenter = LoginPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        btnSign.setOnClickListener {
            if(isValid()){
                loading(true)

                presenter.login(txtEmail.text.toString(), txtPassword.text.toString())
            }
        }
    }

    private fun isValid(): Boolean {
        if(txtEmail?.text.toString().isEmpty()) return false
        if(txtPassword?.text.toString().isNullOrEmpty()) return false
        return true
    }

    override fun onSuccess() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loading(value: Boolean){
        if(value) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE

        if(!value){
            txtEmail.text?.clear()
            txtPassword.text?.clear()
        }

        if(!value) inputLayoutEmail.visibility = View.VISIBLE
        else inputLayoutEmail.visibility = View.GONE

        if(!value) inputLayoutPassword.visibility = View.VISIBLE
        else inputLayoutPassword.visibility = View.GONE
    }

    override fun onFailed(e: String) {
        loading(false)
        Snackbar.make(container, e, Snackbar.LENGTH_LONG)
    }

    override fun onError(e: Throwable) {
        loading(false)
        Snackbar.make(container, e.message.toString(), Snackbar.LENGTH_LONG)
    }
}
