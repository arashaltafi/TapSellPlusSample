package com.arash.altafi.tapsellplussample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.tapsellplussample.databinding.ActivityNativeBannerBinding
import ir.tapsell.plus.AdHolder
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

class NativeBannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNativeBannerBinding
    private val TAG = "NativeBannerActivity"
    private var adHolder: AdHolder? = null
    private var responseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeBannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            adHolder = TapsellPlus.createAdHolder(this@NativeBannerActivity, adContainer, R.layout.native_banner)
            requestAd()
        }
    }

    private fun requestAd() {
        TapsellPlus.requestNativeAd(
            this,
            BuildConfig.TAPSELL_NATIVE_BANNER,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    if (isDestroyed) return
                    Log.d(TAG, "Ad Response")
                    responseId = tapsellPlusAdModel.responseId
                    showAd()
                }

                override fun error(message: String) {
                    if (isDestroyed) return
                    Log.e(TAG, "error: $message")
                }
            })
    }

    private fun showAd() {
        TapsellPlus.showNativeAd(this, responseId, adHolder,
            object : AdShowListener() {
                override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.onOpened(tapsellPlusAdModel)
                    Log.d(TAG, "Ad Open")
                }

                override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                    super.onError(tapsellPlusErrorModel)
                    Log.e("onError", tapsellPlusErrorModel.toString())
                }
            })
    }

    private fun destroyAd() {
        TapsellPlus.destroyNativeBanner(this@NativeBannerActivity, responseId)
    }

    override fun onDestroy() {
        destroyAd()
        super.onDestroy()
    }

}