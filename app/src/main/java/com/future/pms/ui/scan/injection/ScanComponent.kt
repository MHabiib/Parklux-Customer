package com.future.pms.ui.scan.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.scan.view.ScanFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ScanModule::class])
interface ScanComponent {
  fun inject(scanFragment: ScanFragment)
}