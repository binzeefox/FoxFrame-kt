@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.utils

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresPermission
import com.binzeefox.kt_foxframe.core.FoxCore
import java.net.NetworkInterface
import java.net.SocketException

/**
 * 手机状态
 *
 * @author 狐彻
 * 2020/09/26 21:50
 */
object StatusInfoUtil {
    const val NETWORK_NONE = 0  //无网络
    const val NETWORK_DATA = 1  //数据网络
    const val NETWORK_WIFI = 2  //无线网络
    const val NETWORK_OTHER = 9 //其它网络

    // 连接状态管理器
    // @author 狐彻 2020/09/26 22:15
    val connectivityManager: ConnectivityManager?
        get() = FoxCore.application.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager?

    // 位置信息管理器
    // @author 狐彻 2020/09/26 22:52
    val locationManager: LocationManager?
        get() = FoxCore.application.getSystemService(Context.LOCATION_SERVICE)
                as LocationManager?

    // 活动管理器
    // @author 狐彻 2020/09/26 22:58
    val activityManager: ActivityManager?
        get() = FoxCore.application.getSystemService(Activity.ACTIVITY_SERVICE)
                as ActivityManager?

    ///////////////////////////////////////////////////////////////////////////
    // 网络相关
    ///////////////////////////////////////////////////////////////////////////

    // 是否连接网络
    // @author 狐彻 2020/09/26 22:12
    val isNetworkConnected: Boolean
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() {
            connectivityManager ?: return false
            val network = connectivityManager?.activeNetwork ?: return false
            connectivityManager?.getNetworkCapabilities(network)?.also {
                return it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }
            return false
        }


    // 网络连接状态
    // @author 狐彻 2020/09/26 21:55
    val networkState: Int
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() {
            if (!isNetworkConnected) return NETWORK_NONE
            connectivityManager?.activeNetwork?.also {
                val capabilities = connectivityManager?.getNetworkCapabilities(it)
                    ?: return NETWORK_NONE
                return when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                        NETWORK_DATA
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                        NETWORK_WIFI
                    else -> NETWORK_OTHER
                }
            }
            return NETWORK_NONE
        }

    /**
     * 注册网络监听
     *
     * @author 狐彻 2020/09/26 22:46
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun registerNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager?.registerDefaultNetworkCallback(callback)
    }

    /**
     * 注销网络监听
     *
     * @author 狐彻 2020/09/26 22:46
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun unregisterNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        connectivityManager?.unregisterNetworkCallback(callback)
    }

    /**
     * 拓展函数，注册网络监听
     *
     * @author 狐彻 2020/09/26 22:43
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun ConnectivityManager.NetworkCallback.register() {
        registerNetworkCallback(this)
    }

    /**
     * 拓展函数，注销网络监听
     *
     * @author 狐彻 2020/09/26 22:43
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun ConnectivityManager.NetworkCallback.unregister() {
        unregisterNetworkCallback(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GPS相关
    ///////////////////////////////////////////////////////////////////////////

    // GPS是否开启
    // @author 狐彻 2020/09/26 22:55
    val isGPSEnabled: Boolean
        get() = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
            ?: false

    // 地理信息是否开启
    // @author 狐彻 2020/09/26 22:55
    val isLocationEnabled: Boolean
        get() = locationManager?.isLocationEnabled ?: false

    ///////////////////////////////////////////////////////////////////////////
    // 内存
    ///////////////////////////////////////////////////////////////////////////

    // 获取可用内存kb值
    // @author 狐彻 2020/09/26 22:56
    val freeMemoryKBs: Long
        get() {
            ActivityManager.MemoryInfo().also {
                activityManager ?: return -1
                activityManager?.getMemoryInfo(it)
                return it.availMem / 1024
            }
        }

    ///////////////////////////////////////////////////////////////////////////
    // 软键盘
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 显示软键盘
     *
     * @author 狐彻 2020/09/28 11:09
     */
    fun showSoftKeyboard(view: View) {
        if (!view.requestFocus()) return
        val imm: InputMethodManager? =
            FoxCore.application.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    ///////////////////////////////////////////////////////////////////////////
    // IP相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * IP工具类
     *
     * @author 狐彻 2020/09/28 11:15
     */
    object IPConfig {
        private const val TAG = "StatusInfoUtil"

        // IP地址
        // @author 狐彻 2020/09/28 11:15
        val ipAddress: String?
            get() = when (networkState) {
                NETWORK_NONE -> null
                NETWORK_DATA -> getDataIPAddress()
                NETWORK_WIFI -> getWifiIPAddress()
                else -> null
            }

        /**
         * IP字符串转数字
         *
         * @author 狐彻 2020/09/28 11:27
         */
        fun ipToInteger(ipStr: String): Long {
            ipStr.split(".").also { ip ->
                return (ip[0].toLong() shl 24) + (ip[1].toLong() shl 16) + (ip[2].toLong() shl 8) + ip[3].toLong()
            }
        }

        /**
         * IP数字转字符串
         *
         * @author 狐彻 2020/09/28 11:35
         */
        fun integerToIp(intIp: Long): String =
            "${intIp shr 24}.${intIp and 0x00FFFFFF shr 16}.${intIp and 0x0000FFFF shr 8}.${intIp and 0x000000FF}"


        ///////////////////////////////////////////////////////////////////////////
        // 私有方法
        ///////////////////////////////////////////////////////////////////////////

        /**
         * 获取移动数据IP地址
         *
         * @author 狐彻 2020/09/28 11:17
         */
        private fun getDataIPAddress(): String? {
            try {
                val en = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress) {
                            return inetAddress.hostAddress
                        }
                    }
                }
            } catch (e: SocketException) {
                LogUtil.e(TAG, "getDataIPAddress: ", e)
                return null
            }
            return null
        }

        /**
         * 获取wifi的IP地址
         *
         * @author 狐彻 2020/09/28 11:23
         */
        private fun getWifiIPAddress(): String? {
            val manager: WifiManager? = FoxCore.application.getSystemService(Context.WIFI_SERVICE)
                    as WifiManager?
            val info = manager?.connectionInfo ?: return null
            return info.ipAddress.toIPString()
        }

        ///////////////////////////////////////////////////////////////////////////
        // 拓展函数
        ///////////////////////////////////////////////////////////////////////////

        fun Number.toIPString(): String = integerToIp(this as Long)

        fun String.toIPString(ip: Long): String = integerToIp(ip)
    }
}