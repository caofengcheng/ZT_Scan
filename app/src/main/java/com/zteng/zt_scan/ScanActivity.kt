package com.zteng.zt_scan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.storage.StorageManager
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.huagaoscan.sdk.HGScanManager
import com.huagaoscan.sdk.api.PreviewCallback
import com.huagaoscan.sdk.def.EventDef
import com.zteng.common.base.BaseActivity
import com.zteng.common.util.Sp
import com.zteng.mvp.entity.BitmapInfo
import com.zteng.zt_scan.adapter.ScanAdapter
import com.zteng.zt_scan.contract.UploadContract
import com.zteng.zt_scan.presenter.UploadPresenter
import kotlinx.android.synthetic.main.activity_scan.*
import java.io.File


class ScanActivity : BaseActivity<UploadPresenter>(), UploadContract.View {

    private val IMG_DIR = "/sdcard/picture"
    private var isScanning = true
    private var bmpSize = 0
    private var mList = mutableListOf<BitmapInfo>()
    private var mUsbDir: String? = null
    private var mAdapter: ScanAdapter = ScanAdapter(this, mList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        init()
    }

    override val presenter = UploadPresenter()

    override fun initView() {
        mPresenter!!.setView(this)
        bmpSize = resources.displayMetrics.widthPixels / 4
        rv_preview.adapter = mAdapter
        rv_preview.layoutManager = GridLayoutManager(this, 4)
        setOnclick()
    }

    override fun initData() {
        checkUdisk()
        initScanSavePath()
        HGScanManager.getInstance()
            .setPreviewCallback({ index, image ->
                Log.d(this@ScanActivity.toString(), "onPreview index: $index")
                val bitmapInfo = BitmapInfo()
                bitmapInfo.index = index
                bitmapInfo.path = image as String
                val msg = Message()
                msg.what = 1
                msg.obj = bitmapInfo
                mHandler.sendMessage(msg)
            }, PreviewCallback.FORMAT_JPEG_FILE)
    }

    override fun showData() {
    }

    override fun releaseData() {
    }

    /**
     *上传成功
     */
    override fun isSuccess(success: Boolean) {

        //上传成功
        if (success) {
            Toast.makeText(this, R.string.uploadSuccess, Toast.LENGTH_SHORT).show()
            finish()
        } else { //上传失败
            Toast.makeText(this, R.string.uploadFail, Toast.LENGTH_SHORT).show()
            btn_stop.isEnabled = true
            btn_back.isEnabled = false
        }
    }


    var mHandler: Handler =
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var info: BitmapInfo? = null
                if (msg.obj != null) {
                    info = msg.obj as BitmapInfo
                    mList.add(info)
                    mAdapter.notifyItemInserted(mList.size - 1)
                    rv_preview!!.scrollToPosition(mList.size - 1)
                }
            }
        }

    var mReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                when (intent.action) {
                    "com.hugaaoscan.finished" -> {
                        isScanning = false
                        Toast.makeText(
                            context,
                            R.string.scanFinished,
                            Toast.LENGTH_SHORT
                        ).show()
                        btn_stop.isEnabled = true
                        btn_back.isEnabled = false
                        btn_stop.text = "开始上传"
                    }
                    EventDef.STATE_ERROR, EventDef.STATE_ERROR_JAM -> {
                        val message: String
                        val code = intent.getIntExtra("code", -1)
                        message = if (code == EventDef.S_EVT_JAM_IN) {
                            "搓纸失败！"
                        } else if (code == EventDef.S_EVT_DOUBLEPAPER) {
                            "检测到双张，停止扫描！"
                        } else if (code == EventDef.S_EVT_JAM_OUT) {
                            "卡纸了，已停止扫描！"
                        } else {
                            "扫描异常！"
                        }
                        if (isScanning) {
                            isScanning = false
                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        btn_back.isEnabled = true
                    }
                }
            }
        }


    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.hugaaoscan.finished")
        intentFilter.addAction("com.hugaaoscan.error")
        intentFilter.addAction(EventDef.STATE_ERROR)
        intentFilter.addAction(EventDef.STATE_ERROR_JAM)
        registerReceiver(mReceiver, intentFilter)
        Log.d(this@ScanActivity.toString(), "onResume: " + Application.isScanning)
        btn_back.isEnabled = Application.isScanning
        btn_stop.isEnabled = Application.isScanning
    }

    private fun checkUdisk() {
        val storageManager = getSystemService("storage") as StorageManager
        val list = storageManager.storageVolumes
        var hasUdisk = false
        for (i in list.indices) {
            val volume = list[i]
            try {
                val getPath =
                    volume.javaClass.getDeclaredMethod("getPath")
                if (volume.isRemovable) {
                    hasUdisk = true
                    Log.d(this@ScanActivity.toString(), "checkUdisk: " + getPath.invoke(volume))
                    mUsbDir = getPath.invoke(volume) as String
                    break
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.d("ScanActivity", "checkUdisk: $hasUdisk")
    }

    private fun initScanSavePath() {
        val curUsbEnable: Boolean = Sp.getBoolean(this, Define.SP_TRANSPORT_UDISK, false)
        if (mUsbDir != null && curUsbEnable) {
            val file = File(mUsbDir)
            if (file.exists()) {
                HGScanManager.getInstance().setSavePath("$mUsbDir/picture", "Doc")
                return
            }
        }
        HGScanManager.getInstance().setSavePath(IMG_DIR, "Doc")
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(mReceiver)
    }

    override fun onBackPressed() {
        if (isScanning) {
            Toast.makeText(
                this,
                "当前正在扫描中，请先结束操作！",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        super.onBackPressed()
    }

    /**
     * 返回
     */
    fun btnBack(view: View) {
        finish()
    }

    fun setOnclick() {
        /**
         * 停止扫描
         */
        btn_stop.setOnClickListener {
            if (Application.isScanning) {
                HGScanManager.getInstance().stopScan()
                btn_stop.isEnabled = false
            } else {
                mPresenter!!.upload(mList)
                btn_stop.isEnabled = false
                btn_back.isEnabled = true
            }
        }
    }


    override fun onDestroy() { //释放预览接口以避免可能的内存泄露
        HGScanManager.getInstance().releasePreviewCallback()
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }


}
