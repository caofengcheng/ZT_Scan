package com.zteng.common.util


import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.StringUtils
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

/**
 * 获取Mac地址
 */
object MacAddress {
    var ECARDNO: String? = null


    /**
     * 获取工具中的mac地址
     * @return
     */
    val deviceMacAddrress: String?
        get() {
            val macAddress: String? = null
            if (!StringUtils.isEmpty(DeviceUtils.getMacAddress())) {
                ECARDNO = StringUitl.replaceString(DeviceUtils.getMacAddress(), ":")
                Log.d("","utilCode --- getMAC:" + ECARDNO!!)
                return ECARDNO
            }
            return macAddress
        }

    /**
     * 6.0以下获取mac地址
     */
    @SuppressLint("HardwareIds")
    fun getMacAddress(context: Context): String? {
        var macAddress: String? = null
        var fstream: FileReader?
        try {
            try {
                fstream = FileReader("/sys/class/net/eth0/address")
            } catch (e: FileNotFoundException) {
                fstream = FileReader("/sys/class/net/wlan0/address")
            }

            var `in`: BufferedReader? = null
            if (fstream != null) {
                try {
                    `in` = BufferedReader(fstream, 1024)
                    macAddress = `in`.readLine()
                } catch (e: IOException) {
                } finally {
                    try {
                        fstream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (`in` != null) {
                        try {
                            `in`.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }
            }

            if (StringUtils.isEmpty(macAddress)) {
                val wifiMgr = context.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                val info = wifiMgr.connectionInfo
                if (null != info) macAddress = info.macAddress
            }

            if (!StringUtils.isEmpty(macAddress)) {
                ECARDNO = StringUitl.replaceString(macAddress, ":")
                Log.d("","getMAC:" + ECARDNO!!)
            }
            return ECARDNO
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ECARDNO
    }


}
