package com.future.pms.ui.scan

import javax.inject.Inject

class ScanPresenter @Inject constructor(){
    private lateinit var view: ScanContract

    fun attach(view: ScanContract) {
        this.view = view
    }

}