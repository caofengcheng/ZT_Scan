package com.zteng.zt_scan.fragment


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zteng.zt_scan.R
import kotlinx.android.synthetic.main.fragment_wifi.*


class WifiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wifi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_wifi_setting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) //进入无线网络配置界面
        }
    }

}
