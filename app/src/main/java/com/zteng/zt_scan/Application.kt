package com.zteng.zt_scan

import android.app.Application
import android.content.Intent
import android.util.Log
import com.huagaoscan.sdk.HGScanManager
import com.huagaoscan.sdk.api.ScanEventListener
import com.huagaoscan.sdk.def.EventDef
import com.zteng.mvp.GlobalParameter
import com.zteng.mvp.RestManager
import org.slf4j.LoggerFactory

/**
 *
 * @author caofengcheng on 2020-07-01
 */
open class Application : Application() {

    companion object{
        var isScanning = false
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("HGScanManager", "onCreate: ")
        HGScanManager.getInstance().init(this)
        HGScanManager.getInstance().setScanEventListener(mScanEventListener)
        RestManager.initRest(GlobalParameter.jcAddress)
    }

    //扫描仪事件监听
    private val mScanEventListener: ScanEventListener =
        ScanEventListener { code, status ->
            Log.d("Application","onEvent: status:$status;  code:$code")
            when (status) {
                EventDef.STATE_SDK_INIT -> sendBroadcast(Intent(EventDef.STATE_SDK_INIT))
                EventDef.STATE_STANDBY -> {
                    sendBroadcast(Intent(EventDef.STATE_STANDBY))
                    isScanning = false
                }
                EventDef.STATE_PAPER_READY -> sendBroadcast(Intent(EventDef.STATE_PAPER_READY))
                EventDef.STATE_COVER_OPEN -> sendBroadcast(Intent(EventDef.STATE_COVER_OPEN))
                EventDef.STATE_SCANNING -> if (code == EventDef.S_EVT_START_SCAN) { //开始扫描了
                    isScanning = true
                    val intent = Intent(
                        applicationContext,
                        ScanActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    sendBroadcast(Intent("com.hugaaoscan.startscan"))
                }
                EventDef.STATE_ERROR, EventDef.STATE_ERROR_JAM -> {
                    isScanning = false
                    val intentError = Intent(status)
                    intentError.putExtra("code", code)
                    sendBroadcast(intentError)
                }
                "CorrectSate" -> {
                    val intent =
                        Intent("com.huagaoscan.correct")
                    intent.putExtra("event", code)
                    sendBroadcast(intent)
                }
                else -> {
                }
            }
            if (code == EventDef.S_EVT_SCAN_STOPPED) {
                isScanning = false
                sendBroadcast(Intent("com.hugaaoscan.finished"))
            }
        }


}