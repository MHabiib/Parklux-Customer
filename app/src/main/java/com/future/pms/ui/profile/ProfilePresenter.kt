package com.future.pms.ui.profile

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.future.pms.util.Authentication
import javax.inject.Inject

class ProfilePresenter @Inject constructor() {

    private lateinit var view: ProfileContract

    private fun getContext(): Context {
        return when (view) {
            is Fragment -> (view as Fragment).context!!
            is Activity -> (view as Activity)
            else -> throw Exception()
        }
    }

    fun signOut() {
        Authentication.delete(getContext())
    }

    fun attach(view: ProfileContract) {
        this.view = view
    }

    fun subscribe() {}
}