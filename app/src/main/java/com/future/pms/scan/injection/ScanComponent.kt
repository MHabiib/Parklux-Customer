package com.future.pms.scan.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.scan.view.ScanFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ScanModule::class])
interface ScanComponent {
  fun inject(scanFragment: ScanFragment)
}