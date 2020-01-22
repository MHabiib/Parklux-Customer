package com.future.pms.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function

fun <T> Observable<T>.subscribeWithRetryWhen(
    retryHandler: Function<Observable<out Throwable>, Observable<*>>): Observable<T> = this.observeOn(
    AndroidSchedulers.mainThread()).retryWhen(retryHandler)