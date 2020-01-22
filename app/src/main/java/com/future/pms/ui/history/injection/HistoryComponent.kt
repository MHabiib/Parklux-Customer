package com.future.pms.ui.history.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.history.view.HistoryFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [HistoryModule::class])
interface HistoryComponent {
  fun inject(historyFragment: HistoryFragment)
}