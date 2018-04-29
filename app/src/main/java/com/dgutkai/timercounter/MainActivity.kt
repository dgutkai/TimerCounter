package com.dgutkai.timercounter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.content.SharedPreferences
import android.view.Menu
import com.dgutkai.timercounter.constant.SPKey
import com.dgutkai.timercounter.utils.SPUtils
import java.util.*
import android.R.menu
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.RemoteViews
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {

    lateinit var calendarView: CalendarView
    var targetDate: Long = Date().time
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView = findViewById(R.id.calendar_view)
        calendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener{
            override fun onSelectedDayChange(p0: CalendarView?, p1: Int, p2: Int, p3: Int) {
                val selectData = String.format("%d:%02d:%02d", p1, p2, p3)
                val c = Calendar.getInstance()
                c.set(p1, p2, p3, 0, 0, 0)
                targetDate = c.timeInMillis

            }

        })
        val target = SPUtils.getLong(this, SPKey.SHARED_TARGET_DATA)
        if (target != -1L){
            targetDate = target
        }
        calendarView.date = targetDate

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
            R.id.action_save -> {
                val c = Calendar.getInstance()
                val dx = (targetDate - c.timeInMillis)/1000/3600/24 + 1

                SPUtils.setLong(this, SPKey.SHARED_TARGET_DATA, targetDate)
                val componentName = ComponentName(this, NewAppWidget::class.java)
                val remoteViews = RemoteViews(getPackageName(), R.layout.new_app_widget)
                remoteViews.setTextViewText(R.id.appwidget_text, "倒计时\r\n$dx 天")
                //由AppWidgetManager处理Wiget。
                val awm = AppWidgetManager.getInstance(this)
                awm.updateAppWidget(componentName, remoteViews)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
