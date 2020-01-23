package com.future.pms.receipt.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.receipt.view.ReceiptFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ReceiptModule::class])
interface ReceiptComponent {
  fun inject(receiptFragment: ReceiptFragment)
}