package com.whynuttalk.foreignteacher.util

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by cz on 3/27/18.
 */
class Lifecycle {

    private val startedSubject = BehaviorSubject.createDefault(false)

    val started: Observable<Boolean>
        get() = startedSubject

    fun peekStarted(): Boolean = startedSubject.value

    fun notifyStarted() {
        startedSubject.onNext(true)
    }

    fun notifyStopped() {
        startedSubject.onNext(false)
    }
}

fun <T> Observable<T>.bind(lifecycle: Lifecycle): Observable<T> {
    return lifecycle.started
            .distinctUntilChanged()
            .switchMap {
                if (it) {
                    this
                } else {
                    Observable.empty()
                }
            }
}

fun <T> Single<T>.bind(lifecycle: Lifecycle): Single<T> {
    return lifecycle.started
            .takeUntil { !it }
            .switchMap {
                if (it) {
                    this.toObservable()
                } else {
                    Observable.empty()
                }
            }
            .firstOrError()
}

fun <T> Maybe<T>.bind(lifecycle: Lifecycle): Maybe<T> {
    return lifecycle.started
            .takeUntil { !it }
            .switchMap {
                if (it) {
                    this.toObservable()
                } else {
                    Observable.empty()
                }
            }
            .firstElement()
}

fun Completable.bind(lifecycle: Lifecycle): Completable {
    return Completable.fromObservable(
            lifecycle.started
                    .takeUntil { !it }
                    .switchMap {
                        if (it) {
                            this.toObservable<Unit>()
                        } else {
                            Observable.empty()
                        }
                    }
    )
}


interface LifecycleOwner {
    val lifecycle: Lifecycle
}