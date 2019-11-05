package com.future.pms.network

import com.future.pms.model.Post
import io.reactivex.Observable
import retrofit2.http.*



interface ApiServiceInterface {

    @GET("posts")
    fun getPostList(): Observable<List<Post>>
}
