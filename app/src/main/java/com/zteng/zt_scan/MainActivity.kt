package com.zteng.zt_scan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.huagaoscan.sdk.HGScanManager
import com.huagaoscan.sdk.def.EventDef
import com.huagaoscan.sdk.def.ScanDef
import com.zteng.common.util.Paper
import kotlinx.android.synthetic.main.activity_main.*
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity() {
    var isScan: Boolean = false //卡纸是否就绪

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        printDisplayInfo()
        initReceiver()
        val displayMetrics = resources.displayMetrics
        Log.d("MainActivity","onCreate w: " + displayMetrics.widthPixels + "; h: " + displayMetrics.heightPixels)
        Log.d("MainActivity","onCreate pages: " + HGScanManager.getInstance().wheelPages)
        Log.d("MainActivity","onCreate: " + Paper.values()[0].name)
        //图像四角不填充为背景色
        //HGScanManager.getInstance().setAdjustOrientation(true);
        //开启文本mark点方向识别
        HGScanManager.getInstance().isAdjustOrientation = false
    }

    /**
     * 监听卡纸
     */
    private fun initReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(EventDef.STATE_SDK_INIT)
        intentFilter.addAction(EventDef.STATE_STANDBY)
        intentFilter.addAction(EventDef.STATE_PAPER_READY)
        intentFilter.addAction(EventDef.STATE_COVER_OPEN)
        intentFilter.addAction(EventDef.STATE_SCANNING)
        intentFilter.addAction(EventDef.STATE_ERROR)
        intentFilter.addAction(EventDef.STATE_ERROR_JAM)
        intentFilter.addAction(EventDef.STATE_CREATE_CORRECT)
        registerReceiver(mReceiver, intentFilter)
    }

    /**
     * 清除错误
     */
    fun chearBt(view: View) {
        HGScanManager.getInstance().operate(ScanDef.CMD.CLEAR_ERROR)
        HGScanManager.getInstance().operate(ScanDef.CMD.RESET)
    }

    /**
     * 设备复位
     */
    fun restorationBt(view: View) {
        HGScanManager.getInstance().operate(ScanDef.CMD.RESET)
    }

    /**
     * 卡纸复位
     */
    fun paperBt(view: View) {
        HGScanManager.getInstance().operate(ScanDef.CMD.PULL_PAPER)
    }

    /**
     * 扫描设置
     */
    fun scannerBt(view: View) {
        val intent = Intent(this,ScannerSetActivity::class.java)
        startActivity(intent)
    }

    /**
     * 系统设置
     */
    fun setBt(view: View) {
        val intent = Intent(this,SystemSettingActivity::class.java)
        startActivity(intent)
    }

    /**
     * 开始扫描
     */
    fun startScanLl(view: View) {
        if (isScan) HGScanManager.getInstance().operate(ScanDef.CMD.START)
    }

    /**
     * test code:查看设备显示信息 以便进行ui绘制
     */
    private fun printDisplayInfo() {
        val dpi = resources.displayMetrics.densityDpi
        val xDpi = resources.displayMetrics.xdpi
        val yDpi = resources.displayMetrics.ydpi
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        Log.d("MainActivity",
            "printDisplayInfo: dpi=" + dpi + ";xDpi=" +
                    xDpi + ";yDpi=" + yDpi + ";width=" + width + ";height=" + height + ";version:" + Build.VERSION.RELEASE
        )
    }



    private var mReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == null) return
                when (intent.action) {
                    EventDef.STATE_SDK_INIT -> {//SDK初始化 成功 可以获取扫描仪的信息以及执行操作了
                    }
                    EventDef.STATE_STANDBY -> {  // 空闲
                        start_scan_ll.setBackgroundColor(Color.parseColor("#0A000000"))
                        start_scan_tv.setTextColor(Color.parseColor("#40000000"))
                        start_scan_iv.setBackgroundResource(R.mipmap.scanner_gray)
                        start_scan_tv.text = "放入纸张"
                        isScan = false
                    }
                    EventDef.STATE_PAPER_READY -> {// 就绪（纸张放好）
                        start_scan_ll.setBackgroundColor(Color.parseColor("#1890FF"))
                        start_scan_tv.setTextColor(Color.parseColor("#FFFFFF"))
                        start_scan_tv.text = "开始扫描"
                        start_scan_iv.setBackgroundResource(R.mipmap.scanner_white)
                        isScan = true
                    }
                    EventDef.STATE_COVER_OPEN -> {// 纸盒打开
                    }
                    EventDef.STATE_ERROR, EventDef.STATE_ERROR_JAM -> {
                    }
                }
            }
        }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }


}
