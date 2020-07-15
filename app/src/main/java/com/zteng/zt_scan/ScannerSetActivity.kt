package com.zteng.zt_scan

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.huagaoscan.sdk.HGScanManager
import com.huagaoscan.sdk.ScanSetting
import com.huagaoscan.sdk.def.ScanDef
import kotlinx.android.synthetic.main.activity_scanner_set.*


class ScannerSetActivity : AppCompatActivity() {
    val sp = arrayListOf(*Paper.values())
    private var scanSetting: ScanSetting = HGScanManager.getInstance().scanSetting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner_set)
        initView()
        checkBoxChange()
    }

    private fun initView() {
        Log.d("ScannerSetActivity", "init-quality: " + scanSetting.quality)
        Log.d("ScannerSetActivity", "init-isAutoAdjust: " + scanSetting.isAutoAdjust)
        Log.d("ScannerSetActivity", "init-isAutoCut: " + scanSetting.isAutoCut)
        Log.d("ScannerSetActivity", "init-isDoubleChecked: " + scanSetting.isDoubleChecked)
        Log.d("ScannerSetActivity", "init-isSkipBlank: " + scanSetting.isSkipBlank)

        if (TextUtils.equals(ScanDef.ScanMode.SINGLE, scanSetting.scanMode))//设置模式
            scan_pattern_tv.setText(R.string.scan_one_paper_pattern) else scan_pattern_tv.setText(R.string.scan_two_paper_pattern)

        if (TextUtils.equals(ScanDef.ColorMode.COLOR, scanSetting.colorMode))//设置色彩
            scan_color_tv.setText(R.string.color) else scan_color_tv.setText(R.string.color_black)

        when (scanSetting.quality) {//设置品质
            ScanDef.Qulality.Standard -> scan_quality_tv.setText(R.string.qualityStandard)
            ScanDef.Qulality.High -> scan_quality_tv.setText(R.string.qualityHigh)
            else -> scan_quality_tv.setText(R.string.qualityLow)
        }

        scan_correct_prejudiced_cb.isChecked = scanSetting.isAutoAdjust//是否自动纠偏
        scan_tailor_cb.isChecked = scanSetting.isAutoCut//是否自动裁剪
        scan_two_detection_cb.isChecked = scanSetting.isDoubleChecked//是否双张检测
        scan_blank_paper_cb.isChecked = scanSetting.isSkipBlank//是否跳过空白页
        setSpinner()
    }


    /**
     * 设置纸张尺寸
     */
    private fun setSpinner() {
        val adapter = ArrayAdapter<Paper>(this, R.layout.spinner_text_type, sp)
        adapter.setDropDownViewResource(R.layout.spinner_type)
        scan_paper_size_sp.adapter = adapter
        if (TextUtils.isEmpty(HGScanManager.getInstance().paperType)) {
            HGScanManager.getInstance().setPaperHeight(sp[0].name, sp[0].height)
        } else {
            scan_paper_size_sp.setSelection(Paper.valueOf(HGScanManager.getInstance().paperType).ordinal)
        }
        scan_paper_size_sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val spinner: Spinner = parent as Spinner
                val data = spinner.getItemAtPosition(position)
                Log.i(this.toString(), "选中${position}-----${data}")
                HGScanManager.getInstance()
                    .setPaperHeight(sp[position].name, sp[position].height)
            }

        }
    }

    /**
     * 确认
     */
    fun confirmBt(view: View) {
        HGScanManager.getInstance().setting = scanSetting
        finish()
    }

    /**
     * 取消
     */
    fun cancelBt(view: View) {
        finish()
    }

    /**
     * 扫描模式
     */
    fun scanPatternCl(view: View) {
        if (TextUtils.equals(ScanDef.ScanMode.SINGLE, scanSetting.scanMode)) {
            scanSetting.scanMode = ScanDef.ScanMode.DOUBLE
            scan_pattern_tv.setText(R.string.scan_two_paper_pattern)
        } else {
            scanSetting.scanMode = ScanDef.ScanMode.SINGLE
            scan_pattern_tv.setText(R.string.scan_one_paper_pattern)
        }

    }

    /**
     * 色彩模式
     */
    fun scanColorCl(view: View) {
        if (TextUtils.equals(ScanDef.ColorMode.COLOR, scanSetting.colorMode)) {
            scanSetting.colorMode = ScanDef.ColorMode.BLACKWHITE
            scan_color_tv.setText(R.string.color_black)
        } else {
            scanSetting.colorMode = ScanDef.ColorMode.COLOR
            scan_color_tv.setText(R.string.color)
        }

    }

    /**
     * 品质
     */
    fun scanQualityCl(view: View) {
        when (scanSetting.quality) {//设置品质
            ScanDef.Qulality.Standard -> {
                scanSetting.quality = 2
                scan_quality_tv.setText(R.string.qualityHigh)
            }
            ScanDef.Qulality.High -> {
                scanSetting.quality = 0
                scan_quality_tv.setText(R.string.qualityLow)
            }
            else -> {
                scanSetting.quality = 1
                scan_quality_tv.setText(R.string.qualityStandard)
            }
        }
    }

    /**
     * checkBox切换
     */
    private fun checkBoxChange() {
        //是否自动纠偏
        scan_correct_prejudiced_cb.setOnCheckedChangeListener { _, isChecked ->
            scanSetting.isAutoAdjust = isChecked
        }
        //是否自动裁剪
        scan_tailor_cb.setOnCheckedChangeListener { _, isChecked ->
            scanSetting.isAutoCut = isChecked
        }
        //是否双张检测
        scan_two_detection_cb.setOnCheckedChangeListener { _, isChecked ->
            scanSetting.isDoubleChecked = isChecked
        }
        //是否跳过空白页
        scan_blank_paper_cb.setOnCheckedChangeListener { _, isChecked ->
            scanSetting.isSkipBlank = isChecked
        }
    }
}


