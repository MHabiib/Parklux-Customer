package com.future.pms.history.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.history.view.HistoryFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [HistoryModule::class])
interface HistoryComponent {
  fun inject(historyFragment: HistoryFragment)
}