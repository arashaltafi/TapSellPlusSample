package com.arash.altafi.tapsellplussample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.tapsellplussample.databinding.ActivityInterstitialBinding
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

class InterstitialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInterstitialBinding
    private val TAG = "RewardActivity"
    private var responseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterstitialBinding.inflate(layoutInflater)
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
        }
    }

    private fun requestAd() {
        binding.apply {
            TapsellPlus.requestInterstitialAd(this@InterstitialActivity, BuildConfig.TAPSELL_INTERSTITIAL,
                object : AdRequestCallback() {
                    override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel)
                        if (isDestroyed) return
                        responseId = tapsellPlusAdModel.responseId
                        showLogToDeveloper("response", Log.DEBUG)
                        showButton.isEnabled = true
                    }

                    override fun error(message: String) {
                        if (isDestroyed) return
                        showLogToDeveloper(message, Log.ERROR)
                    }
                })
        }
    }

    private fun showAd() {
        binding.apply {
            TapsellPlus.showInterstitialAd(this@InterstitialActivity, responseId,
                object : AdShowListener() {
                    override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.onOpened(tapsellPlusAdModel)
                        showLogToDeveloper("onOpened", Log.DEBUG)
                    }

                    override fun onClosed(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.onClosed(tapsellPlusAdModel)
                        showLogToDeveloper("onClosed", Log.DEBUG)
                    }

                    override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                        super.onError(tapsellPlusErrorModel)
                        showLogToDeveloper(tapsellPlusErrorModel.toString(), Log.ERROR)
                    }
                })
            showButton.isEnabled = false
        }
    }

    private fun showLogToDeveloper(message: String, logLevel: Int) {
        binding.apply {
            when (logLevel) {
                Log.DEBUG -> Log.d(TAG, message)
                Log.ERROR -> Log.e(TAG, message)
            }
            logTextView.append(message.trimIndent())
        }
    }

}