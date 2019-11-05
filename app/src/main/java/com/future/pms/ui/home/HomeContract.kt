package com.future.pms.ui.home

import com.future.pms.model.DetailsViewModel
import com.future.pms.model.Post
import com.future.pms.di.base.BaseContract

class HomeContract {

    interface View: BaseContract.View {
        fun showProgress(show: Boolean)
        fun showErrorMessage(error: String)
        fun loadDataSuccess(list: List<Post>)
        fun loadDataAllSuccess(model: DetailsViewModel)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun loadData()
        fun loadDataAll()
        fun deleteItem(item: Post)
    }
}