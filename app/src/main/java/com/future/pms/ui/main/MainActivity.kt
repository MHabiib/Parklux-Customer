package com.future.pms.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.future.pms.R
import com.future.pms.di.component.DaggerActivityComponent
import com.future.pms.di.module.ActivityModule
import com.future.pms.ui.bookingdetail.BookingDetailFragment
import com.future.pms.ui.home.HomeFragment
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.parkingdirection.ParkingDirectionFragment
import com.future.pms.ui.profile.ProfileFragment
import com.future.pms.ui.receipt.ReceiptFragment
import com.future.pms.ui.scan.ScanFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract {
  @Inject lateinit var presenter: MainPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    injectDependency()
    presenter.attach(this)
    val navView: BottomNavigationView = findViewById(R.id.nav_view)
    navView.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.navigation_home -> {
          presenter.onHomeIconClick()
        }
        R.id.navigation_scan -> {
          presenter.onScanIconClick()
        }
        R.id.navigation_profile -> {
          presenter.onProfileIconClick()
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
    nav_view.visibility = View.VISIBLE
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
      nav_view.menu.findItem(R.id.navigation_home).isChecked = true
      supportFragmentManager.beginTransaction().disallowAddToBackStack().setCustomAnimations(
        R.anim.enter_from_left,
        R.anim.exit_to_right,
        R.anim.enter_from_right,
        R.anim.exit_to_left
      ).replace(
        R.id.frame,
              HomeFragment().newInstance(), HomeFragment.TAG).commit()
    }
  }

  override fun showScanFragment() {
    nav_view.visibility = View.VISIBLE
    if (supportFragmentManager.findFragmentByTag(ScanFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(R.id.frame,
              ScanFragment().newInstance(), ScanFragment.TAG).commit()
    }
  }

  override fun showProfileFragment() {
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().setCustomAnimations(
        R.anim.enter_from_right,
        R.anim.exit_to_left,
        R.anim.enter_from_left,
        R.anim.exit_to_right
      ).replace(
        R.id.frame,
              ProfileFragment().newInstance(), ProfileFragment.TAG).commit()
    }
  }

  override fun showReceipt(idBooking: String) {
    nav_view.visibility = View.GONE
    val fragment = ReceiptFragment()
    val bundle = Bundle()
    bundle.putString("idBooking", idBooking)
    fragment.arguments = bundle
    supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(
      R.id.frame, fragment, ReceiptFragment.TAG
    ).commit()
  }

  override fun showBookingDetail(idBooking: String) {
    nav_view.visibility = View.GONE
    val fragment = BookingDetailFragment()
    val bundle = Bundle()
    bundle.putString("idBooking", idBooking)
    fragment.arguments = bundle
    if (supportFragmentManager.findFragmentByTag(ParkingDirectionFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().replace(R.id.frame, fragment,
          BookingDetailFragment.TAG).commit()
    }
  }

  override fun showBookingFailed() {
    nav_view.visibility = View.GONE
    if (supportFragmentManager.findFragmentByTag(ParkingDirectionFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(R.id.frame,
          BookingDetailFragment().newInstance(), BookingDetailFragment.TAG).commit()
    }
  }

  override fun showParkingDirection() {
    nav_view.visibility = View.GONE
    if (supportFragmentManager.findFragmentByTag(ParkingDirectionFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(R.id.frame,
              ParkingDirectionFragment().newInstance(), ParkingDirectionFragment.TAG).commit()
    }
  }

  private fun injectDependency() {
    val activityComponent = DaggerActivityComponent.builder().activityModule(
        ActivityModule(this)).build()

    activityComponent.inject(this)
  }
}
