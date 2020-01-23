package com.future.pms.superadmin.activitydetails.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.superadmin.activitydetails.view.ActivityDetailsFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ActivityDetailsModule::class])
interface ActivityDetailsComponent {
  fun inject(activityDetailsFragment: ActivityDetailsFragment)
}