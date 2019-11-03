package com.future.pms.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.future.pms.R
import com.future.pms.di.component.DaggerActivityComponent
import com.future.pms.di.module.ActivityModule
import com.future.pms.ui.profile.ProfileFragment
import com.future.pms.ui.scan.ScanFragment
import com.future.pms.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View{


    @Inject lateinit var presenter: MainContract.Presenter

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

    override fun showHomeFragment() {
        if (supportFragmentManager.findFragmentByTag(HomeFragment.TAG) == null) {
            supportFragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.frame, HomeFragment().newInstance(), HomeFragment.TAG)
                .commit()
        }
    }

    override fun showScanFragment() {
        if (supportFragmentManager.findFragmentByTag(ScanFragment.TAG) == null) {
            supportFragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.frame, ScanFragment().newInstance(), ScanFragment.TAG)
                .commit()
        }
    }

    override fun showProfileFragment() {
        if (supportFragmentManager.findFragmentByTag(ProfileFragment.TAG) == null) {
            supportFragmentManager.beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.frame, ProfileFragment().newInstance(), ProfileFragment.TAG)
                .commit()
        }
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }

}
