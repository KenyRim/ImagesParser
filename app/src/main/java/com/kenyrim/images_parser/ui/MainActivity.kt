package com.kenyrim.images_parser.ui


import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kenyrim.images_parser.App
import com.kenyrim.images_parser.R
import com.kenyrim.images_parser.adapters.MainAdapter
import com.kenyrim.images_parser.consts.TRANSITION_NAME
import com.kenyrim.images_parser.consts.URL
import com.kenyrim.images_parser.util.DateUtil
import com.kenyrim.images_parser.web.Observe
import java.util.*
import kotlin.collections.ArrayList

import android.content.pm.PackageManager
import android.widget.ImageButton


class MainActivity : AppCompatActivity(), MainAdapter.Callback {

    private lateinit var recyclerView: RecyclerView
    private val REQUEST_WRITE_PERMISSION = 786

    private var list0 = ArrayList<List<String>>()
    private lateinit var mainAdapter: MainAdapter

    companion object {
        const val TODAY: Int = 0
        const val YESTERDAY: Int = -1
        const val TOMORROW: Int = +1
    }

    private lateinit var tvDate: TextView
    private lateinit var dateTime: Date
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrew: ImageButton
    private var today:String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appContext = this

        requestPermission()
        recyclerView = findViewById(R.id.rv_main)

        btnNext = findViewById(R.id.btn_next)
        btnPrew = findViewById(R.id.btn_prew)
        tvDate = findViewById(R.id.tv_date)

        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, TODAY)
        dateTime = cal.time

        today =  DateUtil().getDate(decrementDateByOne(dateTime, 0), DateUtil().patternDate)

        val time = DateUtil().getDate(decrementDateByOne(dateTime, 0), DateUtil().patternTime)

        var shift: Int = TODAY
        if (time.toInt() < 12) {
            shift = YESTERDAY
        }

        shiftDate(shift)

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
        val newDate = DateUtil().getDate(decrementDateByOne(dateTime, dayShift), DateUtil().patternDate)
        tvDate.text = newDate
        btnNext.isEnabled = today != newDate

        list0 = arrayListOf(listOf("$URL$newDate/1", "$URL$newDate/2"))

        mainAdapter = MainAdapter(getContent(), this@MainActivity)
        recyclerView.run {
            hasFixedSize()
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = mainAdapter
        }
    }

    private fun decrementDateByOne(date: Date, shift: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, shift)
        dateTime = calendar.time
        return dateTime
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_WRITE_PERMISSION
            )
        } else {

        }
    }


}