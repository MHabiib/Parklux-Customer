package com.future.pms.ui.home2

import com.future.pms.di.base.BaseMVPPresenterImpl
import com.future.pms.util.Authentication

class HomePresenterImpl : BaseMVPPresenterImpl<HomeContract.HomeView>(),
        HomeContract.HomePresenter {

    override fun signOut() {
        Authentication.delete(getContext())

        view?.let { view -> call(view, view::onLogout)}
    }
}