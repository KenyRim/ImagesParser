package com.kenyrim.images_parser


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() , MainAdapter.Callback {

    private lateinit var recyclerView: RecyclerView

    private var date = "2021-09-09/"
    val list0 = listOf(listOf(URL + date + 1, URL + date + 2))

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_main)

        if (NetworkUtil.isNetworkAvailable(this))
        initRecycler()

    }

    private fun initRecycler() {
        recyclerView.run {
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = MainAdapter(getContent(), this@MainActivity)
        }
    }

    private fun getContent(): List<String> = Observe(list0).run()
    override fun onItemClicked(item: String) {
        Log.e("click", if(item.isNotEmpty())item else "aaaaaaaaaaaaaa")
    }

}