package com.arash.altafi.tapsellplussample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.tapsellplussample.databinding.ActivityStandardBannerBinding
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

class StandardBannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStandardBannerBinding
    private val TAG = "StandardActivity"
    private var responseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStandardBannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            showButton.isEnabled = false
            requestButton.setOnClickListener {
                requestAd()
            }
            showButton.setOnClickListener {
                showAd()
            }
            destroyButton.setOnClickListener {
                destroyAd()
            }
        }
    }

    private fun requestAd() {
        binding.apply {
            TapsellPlus.requestStandardBannerAd(
                this@StandardBannerActivity, BuildConfig.TAPSELL_STANDARD_BANNER,
                TapsellPlusBannerType.BANNER_320x50,
                object : AdRequestCallback() {
                    override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel)
                        if (isDestroyed) return
                        Log.d(TAG, "response")
                        responseId = tapsellPlusAdModel.responseId
                        showButton.isEnabled = true
                    }

                    override fun error(message: String) {
                        if (isDestroyed) return
                        Log.e(TAG, "error $message")
                    }
                })
        }
    }

    private fun showAd() {
        binding.apply {
            TapsellPlus.showStandardBannerAd(this@StandardBannerActivity, responseId,
                findViewById(R.id.standardBanner),
                object : AdShowListener() {
                    override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.onOpened(tapsellPlusAdModel)
                        Log.d(TAG, "Ad Opened")
                    }

                    override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                        super.onError(tapsellPlusErrorModel)
                        Log.e(TAG, tapsellPlusErrorModel.toString())
                    }
                })
            showButton.isEnabled = false
        }
    }

    private fun destroyAd() {
        TapsellPlus.destroyStandardBanner(this, responseId, findViewById(R.id.standardBanner))
    }

    override fun onDestroy() {
        destroyAd()
        super.onDestroy()
    }

}