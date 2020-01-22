package com.future.pms.ui.receipt.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.receipt.view.ReceiptFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ReceiptModule::class])
interface ReceiptComponent {
  fun inject(receiptFragment: ReceiptFragment)
}