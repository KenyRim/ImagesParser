package com.kenyrim.images_parser

import android.annotation.SuppressLint
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object ResultList : ArrayList<String>()

class Observe<T>(t: List<T>) {
    private var value = listOf(t)

    @SuppressLint("CheckResult")
    fun run(): List<String> {
        value.toObservable().subscribeBy(
            onNext = { it1 ->
                it1.forEach { it2 ->
                    val list = it2 as List<*>
                    list.forEach {
                        doWork(it.toString())
                    }
                }
            },
            onError = { it.printStackTrace() },
            onComplete = {
                value.forEach {
                    Observe(it)
                }
            }
        )
        return ResultList
    }

    private fun doWork(url: String) {
        runBlocking {
            withContext(Dispatchers.IO) {
                val aaa = Parser().run(url, SELECTOR)
                ResultList.addAll(aaa)

            }
        }
    }
}