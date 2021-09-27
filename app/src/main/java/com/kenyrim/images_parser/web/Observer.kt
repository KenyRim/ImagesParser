package com.kenyrim.images_parser.web

import android.annotation.SuppressLint
import com.kenyrim.images_parser.consts.SELECTOR
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object ResultList : ArrayList<String>()

class Observe<T>(t: ArrayList<T>) {
    private var value = listOf(t)

    @SuppressLint("CheckResult")
    fun run(): ArrayList<String> {
        value.toObservable().subscribeBy(
            onNext = { it ->
                it.forEach { it1 ->
                    val list = it1 as List<*>
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
                val aaa = HTMLParser().run(url, SELECTOR)
                ResultList.addAll(aaa)
            }
        }
    }
}