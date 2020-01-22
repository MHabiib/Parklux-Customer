package com.future.pms.ui.superadmin.activitydetails.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.superadmin.activitydetails.view.ActivityDetailsFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ActivityDetailsModule::class])
interface ActivityDetailsComponent {
  fun inject(activityDetailsFragment: ActivityDetailsFragment)
}