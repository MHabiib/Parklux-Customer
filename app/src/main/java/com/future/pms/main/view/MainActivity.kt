package com.future.pms.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.bookingdetail.view.BookingDetailFragment
import com.future.pms.databinding.ActivityMainBinding
import com.future.pms.home.view.HomeFragment
import com.future.pms.login.view.LoginActivity
import com.future.pms.main.injection.DaggerMainComponent
import com.future.pms.main.injection.MainComponent
import com.future.pms.main.presenter.MainPresenter
import com.future.pms.parkingdirection.view.ParkingDirectionFragment
import com.future.pms.profile.view.ProfileFragment
import com.future.pms.scan.view.ScanFragment
import com.future.pms.util.Constants.Companion.ID_BOOKING
import com.future.pms.util.Constants.Companion.LEVEL_NAME
import com.future.pms.util.Constants.Companion.REQUEST_CAMERA_PERMISSION
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract {
  private var daggerBuild: MainComponent = DaggerMainComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: MainPresenter
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    presenter.attach(this)
    binding.ibNavigationHome.setOnClickListener {
      presenter.onHomeIconClick()
    }
    binding.ibNavigationProfile.setOnClickListener {
      presenter.onProfileIconClick()
    }
    binding.fabScan.setOnClickListener {
      presenter.onScanIconClick()
    }
  }

  private var doubleBackToExitPressedOnce = false

  override fun onBackPressed() {
    if (doubleBackToExitPressedOnce) {
      finishAffinity()
      return
    }

    this.doubleBackToExitPressedOnce = true
    Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show()

    Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
  }

  override fun showLoginPage() {
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
  }

  override fun showHomeFragment() {
    navigationBarVisibility(View.VISIBLE)
    buttonIndicator(View.VISIBLE, View.GONE)
    binding.homeIndicator.visibility = View.VISIBLE
    binding.profileIndicator.visibility = View.GONE
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, HomeFragment().newInstance(),
          HomeFragment.TAG).commit()
    } else {
      supportFragmentManager.run { findFragmentByTag(HomeFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).show(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(ProfileFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ParkingDirectionFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(ParkingDirectionFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ScanFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(ScanFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().remove(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(BookingDetailFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(BookingDetailFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().remove(it).commit()
      }
    }
  }

  override fun showScanFragment() {
    navigationBarVisibility(View.VISIBLE)
    buttonIndicator(View.GONE, View.GONE)
    when (this.let {
      ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA)
    } != PackageManager.PERMISSION_GRANTED) {
      true -> {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION)
      }
      else -> {
        if (supportFragmentManager.findFragmentByTag(ScanFragment.TAG) == null) {
          supportFragmentManager.beginTransaction().add(R.id.frame, ScanFragment().newInstance(),
              ScanFragment.TAG).commit()
        } else {
          supportFragmentManager.run { findFragmentByTag(ScanFragment.TAG) }?.let {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
                R.animator.fade_out).show(it).commit()
          }
        }
        if (supportFragmentManager.findFragmentByTag(BookingDetailFragment.TAG) != null) {
          supportFragmentManager.run {
            findFragmentByTag(BookingDetailFragment.TAG)
          }?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
          }
        }
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
      grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_CAMERA_PERMISSION) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        if (supportFragmentManager.findFragmentByTag(ScanFragment.TAG) == null) {
          supportFragmentManager.beginTransaction().add(R.id.frame, ScanFragment().newInstance(),
              ScanFragment.TAG).commit()
        }
      } else {
        presenter.onHomeIconClick()
      }
    }
  }

  override fun showProfileFragment() {
    buttonIndicator(View.GONE, View.VISIBLE)
    if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, ProfileFragment().newInstance(),
          ProfileFragment.TAG).commit()
    } else {
      supportFragmentManager.run { findFragmentByTag(ProfileFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).show(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(HomeFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ScanFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(ScanFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().remove(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ParkingDirectionFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(ParkingDirectionFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(BookingDetailFragment.TAG) != null) {
      supportFragmentManager.run {
        findFragmentByTag(BookingDetailFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().remove(it).commit()
      }
    }
  }

  override fun showBookingDetail(idBooking: String) {
    navigationBarVisibility(View.GONE)
    val fragment = BookingDetailFragment()
    val bundle = Bundle()
    bundle.putString(ID_BOOKING, idBooking)
    fragment.arguments = bundle
    if (supportFragmentManager.findFragmentByTag(BookingDetailFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, fragment, BookingDetailFragment.TAG).commit()
    } else {
      supportFragmentManager.run {
        findFragmentByTag(BookingDetailFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).show(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ScanFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(ScanFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().remove(it).commit()
      }
    }
  }

  override fun showBookingFailed() {
    navigationBarVisibility(View.GONE)
    if (supportFragmentManager.findFragmentByTag(BookingDetailFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, BookingDetailFragment().newInstance(),
          BookingDetailFragment.TAG).commit()
    } else {
      supportFragmentManager.run {
        findFragmentByTag(BookingDetailFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).show(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(ScanFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(ScanFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().remove(it).commit()
      }
    }
  }

  override fun showParkingDirection(idBooking: String, levelName: String) {
    navigationBarVisibility(View.GONE)
    val fragment = ParkingDirectionFragment()
    val bundle = Bundle()
    bundle.putString(ID_BOOKING, idBooking)
    bundle.putString(LEVEL_NAME, levelName)
    fragment.arguments = bundle
    if (supportFragmentManager.findFragmentByTag(ParkingDirectionFragment.TAG) == null) {
      supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
          R.animator.fade_out).add(R.id.frame, fragment, ParkingDirectionFragment.TAG).commit()
    } else {
      supportFragmentManager.run {
        findFragmentByTag(ParkingDirectionFragment.TAG)
      }?.let {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).show(it).commit()
      }
    }
    if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) != null) {
      supportFragmentManager.run { findFragmentByTag(HomeFragment.TAG) }?.let {
        supportFragmentManager.beginTransaction().hide(it).commit()
      }
    }
  }

  private fun navigationBarVisibility(visibility: Int) {
    binding.navView.visibility = visibility
    binding.fabScan.visibility = visibility
  }

  private fun buttonIndicator(home: Int, profile: Int) {
    binding.homeIndicator.visibility = home
    binding.profileIndicator.visibility = profile
  }
}
