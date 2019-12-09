package com.future.pms.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.future.pms.R
import com.future.pms.databinding.ActivityMainBinding
import com.future.pms.di.component.DaggerActivityComponent
import com.future.pms.di.module.ActivityModule
import com.future.pms.ui.bookingdetail.BookingDetailFragment
import com.future.pms.ui.home.HomeFragment
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.parkingdirection.ParkingDirectionFragment
import com.future.pms.ui.profile.ProfileFragment
import com.future.pms.ui.receipt.ReceiptFragment
import com.future.pms.ui.scan.ScanFragment
import com.future.pms.util.Constants.Companion.ID_BOOKING
import com.future.pms.util.Constants.Companion.LEVEL_NAME
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract {
  @Inject lateinit var presenter: MainPresenter
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    injectDependency()
    presenter.attach(this)
    val navView: BottomNavigationView = binding.navView
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
    binding.navView.visibility = View.VISIBLE
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
      binding.navView.menu.findItem(R.id.navigation_home).isChecked = true
      supportFragmentManager.beginTransaction().disallowAddToBackStack().setCustomAnimations(
        R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left
      ).replace(
        R.id.frame, HomeFragment().newInstance(), HomeFragment.TAG
      ).commit()
    }
  }

  override fun showScanFragment() {
    binding.navView.visibility = View.VISIBLE
    if (supportFragmentManager.findFragmentByTag(ScanFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(
        R.id.frame, ScanFragment().newInstance(), ScanFragment.TAG
      ).commit()
    }
  }

  override fun showProfileFragment() {
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().setCustomAnimations(
        R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right
      ).replace(
        R.id.frame, ProfileFragment().newInstance(), ProfileFragment.TAG
      ).commit()
    }
  }

  override fun showReceipt(idBooking: String) {
    binding.navView.visibility = View.GONE
    val fragment = ReceiptFragment()
    val bundle = Bundle()
    bundle.putString(ID_BOOKING, idBooking)
    fragment.arguments = bundle
    supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(
      R.id.frame, fragment, ReceiptFragment.TAG
    ).commit()
  }

  override fun showBookingDetail(idBooking: String) {
    binding.navView.visibility = View.GONE
    val fragment = BookingDetailFragment()
    val bundle = Bundle()
    bundle.putString(ID_BOOKING, idBooking)
    fragment.arguments = bundle
    if (supportFragmentManager.findFragmentByTag(ParkingDirectionFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().replace(
        R.id.frame, fragment, BookingDetailFragment.TAG
      ).commit()
    }
  }

  override fun showBookingFailed() {
    binding.navView.visibility = View.GONE
    if (supportFragmentManager.findFragmentByTag(ParkingDirectionFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(
        R.id.frame, BookingDetailFragment().newInstance(), BookingDetailFragment.TAG
      ).commit()
    }
  }

  override fun showParkingDirection(idBooking: String, levelName: String) {
    binding.navView.visibility = View.GONE
    val fragment = ParkingDirectionFragment()
    val bundle = Bundle()
    bundle.putString(ID_BOOKING, idBooking)
    bundle.putString(LEVEL_NAME, levelName)
    fragment.arguments = bundle
    if (supportFragmentManager.findFragmentByTag(ParkingDirectionFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(
        R.id.frame, fragment, ParkingDirectionFragment.TAG
      ).commit()
    }
  }

  private fun injectDependency() {
    val activityComponent = DaggerActivityComponent.builder().activityModule(
      ActivityModule(this)
    ).build()

    activityComponent.inject(this)
  }
}
