package com.whynuttalk.foreignteacher.ext

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * Created by cz on 3/28/18.
 */

private val logger = LoggerFactory.getLogger("RxJava")

fun <T> Single<T>.doOnLoadingState(action1: (Boolean) -> Unit): Single<T> {
    return doOnSubscribe { action1(true) }
            .doOnEvent { _, _ -> action1(false) }
}

fun <T> Observable<T>.logErrorAndForget(extraAction : (err : Throwable) -> Unit = {}) : Observable<T> {
    return onErrorResumeNext{ throwable : Throwable ->
        logger.e(throwable) { "Ignored error: " }
        extraAction(throwable)
        Observable.empty<T>()
    }
}

fun <T> Maybe<T>.logErrorAndForget(extraAction : (err : Throwable) -> Unit = {}) : Maybe<T> {
    return onErrorResumeNext{ throwable : Throwable ->
        logger.e(throwable) { "Ignored error: " }
        extraAction(throwable)
        Maybe.empty<T>()
    }
}


fun Completable.logErrorAndForget(extraAction : (err : Throwable) -> Unit = {}) : Completable {
    return onErrorResumeNext { throwable ->
        logger.e(throwable) { "Ignored error: " }
        extraAction(throwable)
        Completable.complete()
    }
}

fun Completable.doOnLoading(action : (Boolean) -> Unit) : Completable {
    return doOnSubscribe { action(true) }
            .doOnEvent { action(false) }
}

fun <T> Observable<T>.doOnLoading(action : (Boolean) -> Unit) : Observable<T> {
    return doOnSubscribe { action(true) }
            .doOnEach { action(false) }
}

fun <T> Single<T>.doOnLoading(action : (Boolean) -> Unit) : Single<T> {
    return doOnSubscribe { action(true) }
            .doOnEvent { _, _ -> action(false) }
}

fun <T> Maybe<T>.doOnLoading(action : (Boolean) -> Unit) : Maybe<T> {
    return doOnSubscribe { action(true) }
            .doOnEvent { _, _ -> action(false) }
}


fun <T> Single<T>.toNetWork() : Single<T> {
    return subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(20, TimeUnit.SECONDS, AndroidSchedulers.mainThread())

}

fun <T> Maybe<T>.toNetWork() : Maybe<T> {
    return subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(20, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
            .doOnError{err->
                    err.toast()
            }
}
