package com.future.pms.ui.profile

import com.future.pms.di.base.BaseMVPPresenterImpl
import com.future.pms.util.Authentication

class ProfilePresenter : BaseMVPPresenterImpl<ProfileContract.View>(), ProfileContract.Presenter {

    override fun signOut() {
        Authentication.delete(getContext())

        view?.let { view -> call(view, view::onLogout)}
    }
}