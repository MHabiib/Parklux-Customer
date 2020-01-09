package com.future.pms.ui.superadmin.mainsuperadmin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.future.pms.R
import com.future.pms.databinding.ActivityMainSuperAdminBinding
import com.future.pms.di.component.DaggerActivityComponent
import com.future.pms.di.module.ActivityModule
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.superadmin.homesuperadmin.HomeFragmentSuperAdmin
import com.future.pms.ui.superadmin.listactivity.ListActivityFragment
import com.future.pms.ui.superadmin.listuser.ListUserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class MainActivitySuperAdmin : AppCompatActivity(), MainContractSuperAdmin {
  @Inject lateinit var presenter: MainPresenterSuperAdmin
  private lateinit var binding: ActivityMainSuperAdminBinding
  private lateinit var navView: BottomNavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main_super_admin)
    injectDependency()
    presenter.attach(this)
    navView = binding.navView
    navView.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.navigation_home -> {
          presenter.onHomeIconClick()
        }
        R.id.navigation_activity -> {
          presenter.onActivityClick()
        }
        R.id.navigation_customer -> {
          presenter.onCustomerClick()
        }
      }
      return@setOnNavigationItemSelectedListener true
    }
  }

  override fun showLoginPage() {
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
  }

  override fun showHomeFragment() {
    if (supportFragmentManager.findFragmentByTag(HomeFragmentSuperAdmin.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().setCustomAnimations(
          R.animator.fade_in, R.animator.fade_out).replace(R.id.frame,
          HomeFragmentSuperAdmin().newInstance(), HomeFragmentSuperAdmin.TAG).commit()
    }
  }

  override fun showListActivityFragment() {
    if (supportFragmentManager.findFragmentByTag(ListActivityFragment.TAG) == null) {
      binding.navView.menu.findItem(R.id.navigation_activity).isChecked = true
      supportFragmentManager.beginTransaction().disallowAddToBackStack().setCustomAnimations(
          R.animator.fade_in, R.animator.fade_out).replace(R.id.frame,
          ListActivityFragment().newInstance(), ListActivityFragment.TAG).commit()
    }
  }

  override fun showListCustomerFragment() {
    if (supportFragmentManager.findFragmentByTag(ListUserFragment.TAG) == null) {
      binding.navView.menu.findItem(R.id.navigation_customer).isChecked = true
      supportFragmentManager.beginTransaction().disallowAddToBackStack().setCustomAnimations(
          R.animator.fade_in, R.animator.fade_out).replace(R.id.frame,
          ListUserFragment().newInstance(), ListUserFragment.TAG).commit()
    }
  }

  private fun injectDependency() {
    val activityComponent = DaggerActivityComponent.builder().activityModule(
        ActivityModule(this)).build()

    activityComponent.inject(this)
  }
}
