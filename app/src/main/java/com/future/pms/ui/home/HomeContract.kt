package com.future.pms.ui.home

import com.future.pms.model.DetailsViewModel
import com.future.pms.model.Post
import com.future.pms.di.base.BaseContract

interface HomeContract {

    fun showProgress(show: Boolean)
    fun showErrorMessage(error: String)
    fun loadDataSuccess(list: List<Post>)
    fun loadDataAllSuccess(model: DetailsViewModel)
    fun showParkingDirectionFragment()
}