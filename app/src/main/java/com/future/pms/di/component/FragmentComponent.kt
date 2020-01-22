package com.future.pms.di.component

import com.future.pms.di.module.FragmentModule
import com.future.pms.ui.superadmin.activitydetails.view.ActivityDetailsFragment
import com.future.pms.ui.superadmin.homesuperadmin.view.HomeFragmentSuperAdmin
import com.future.pms.ui.superadmin.listactivity.view.ListActivityFragment
import com.future.pms.ui.superadmin.listuser.view.ListUserFragment
import com.future.pms.ui.superadmin.userdetails.view.UserDetailsFragment
import dagger.Component

@Component(modules = [FragmentModule::class]) interface FragmentComponent {

  fun inject(homeFragmentSuperAdmin: HomeFragmentSuperAdmin)

  fun inject(listActivityFragment: ListActivityFragment)

  fun inject(listCustomerFragment: ListUserFragment)

  fun inject(activityDetailsFragment: ActivityDetailsFragment)

  fun inject(userDetailsFragment: UserDetailsFragment)
}