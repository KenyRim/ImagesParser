package com.kenyrim.images_parser.ui


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kenyrim.images_parser.R
import com.kenyrim.images_parser.adapters.MainAdapter
import com.kenyrim.images_parser.consts.TRANSITION_NAME
import com.kenyrim.images_parser.consts.URL
import com.kenyrim.images_parser.util.DateUtil
import com.kenyrim.images_parser.web.Observe
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), MainAdapter.Callback {

    private lateinit var recyclerView: RecyclerView

    private var list0 = ArrayList<List<String>>()
    private lateinit var mainAdapter: MainAdapter

    companion object {
        const val TODAY: Int = 0
        const val YESTERDAY: Int = -1
        const val TOMORROW: Int = +1
    }

    private lateinit var tvDate: TextView
    private lateinit var dateTime: Date

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_main)

        val btnNext: Button = findViewById(R.id.btn_next)
        val btnPrew: Button = findViewById(R.id.btn_prew)
        tvDate = findViewById(R.id.tv_date)

        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, TODAY)
        dateTime = cal.time
        shiftDate(TODAY)

        btnNext.setOnClickListener {
            mainAdapter.clear()
            list0 = ArrayList()
            shiftDate(TOMORROW)
        }

        btnPrew.setOnClickListener {
            mainAdapter.clear()
            list0 = ArrayList()
            shiftDate(YESTERDAY)
        }

    }

    private fun getContent(): ArrayList<String> = Observe(list0).run()

    override fun onItemClicked(item: String, view: ImageView) {
        val intent = Intent(this, PhotoActivity::class.java)
        intent.putExtra(TRANSITION_NAME, item)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@MainActivity,
            view,
            item
        )
        startActivity(intent, options.toBundle())
    }

    private fun shiftDate(dayShift: Int) {
        val date = DateUtil().getDate(decrementDateByOne(dateTime, dayShift))
        tvDate.text = date

        list0 = arrayListOf(listOf("$URL$date/1", "$URL$date/2"))

        Log.e("click", if (date.isNotEmpty()) date else "aaaaaaaaaaaaaa")

        mainAdapter = MainAdapter(getContent(), this@MainActivity)
        recyclerView.run {
            hasFixedSize()
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = mainAdapter
        }
    }

    private fun decrementDateByOne(date: Date, shift: Int): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, shift)
        dateTime = c.time
        return dateTime
    }


}