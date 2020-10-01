package com.binzeefox.kt_foxframe.core.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.TimeUnit

/**
 * Retrofit网络工具
 *
 * TODO 协程支持
 * @author 狐彻
 * 2020/09/25 14:50
 */
open class RetrofitClient protected constructor() {

    // 静态池
    // @author 狐彻 2020/09/25 15:00
    companion object {
        private const val DEFAULT_CONNECT_TIMEOUT: Long = 10
        private const val DEFAULT_READ_TIMEOUT: Long = 20
        private const val DEFAULT_WRITE_TIMEOUT: Long = 60

        // 双重锁单例
        // @author 狐彻 2020/09/25 9:02
        val instance: RetrofitClient   //单例
                by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED)
                { RetrofitClient() }
    }

    // Retrofit实例
    // @author 狐彻 2020/09/25 14:59
    private val mRetrofit: Retrofit by lazy { createRetrofit() }

    // OkHttpClient实例
    // @author 狐彻 2020/09/25 15:06
    private val mClient: OkHttpClient by lazy { createOkHttpClient() }


    ///////////////////////////////////////////////////////////////////////////
    // 可继承方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 构造器
     *
     * @author 狐彻 2020/09/25 14:53
     */
    open fun createRetrofit(): Retrofit = Retrofit.Builder()
        .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(getBaseUrl())
        .build()

    /**
     * 构造OkHttpClient
     *
     * @author 狐彻 2020/09/25 15:03
     */
    open fun createOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
            .also { it.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(onCreateDefaultOkHttpClientInterceptor())
            .build()
    }

    /**
     * 获取基本路径，默认为空，若要实现请继承该类
     *
     * @author 狐彻 2020/09/25 15:08
     */
    open fun getBaseUrl(): String = ""

    /**
     * 添加默认Client的自定义拦截器，默认实现json头
     *
     * @author 狐彻 2020/09/25 15:19
     */
    open fun onCreateDefaultOkHttpClientInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json;charset=UTF-8")
            .build()
        chain.proceed(request)
    }

    ///////////////////////////////////////////////////////////////////////////
    // 公共方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 构建api方法
     *
     * @author 狐彻 2020/09/25 15:44
     */
    fun <T> getApi(api: Class<T>): T = mRetrofit.create(api)
}