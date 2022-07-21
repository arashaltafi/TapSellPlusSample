package com.arash.altafi.tapsellplussample

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.tapsellplussample.databinding.ActivityNativeVideoBinding
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusVideoAdHolder
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

class NativeVideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNativeVideoBinding
    private var responseId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNativeVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            requestButton.setOnClickListener {
                requestAd()
            }
            showButton.setOnClickListener {
                showTheRequestedAd()
            }
            removeButton.setOnClickListener {
                removeAd()
            }
        }
    }

    private fun requestAd() {
        TapsellPlus.requestNativeVideo(
            this,
            BuildConfig.TAPSELL_NATIVE_VIDEO,
            object : AdRequestCallback() {
                override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                    super.response(tapsellPlusAdModel)
                    Toast.makeText(this@NativeVideoActivity, "Ad is ready", Toast.LENGTH_SHORT)
                        .show()
                    responseId = tapsellPlusAdModel.responseId
                }

                override fun error(s: String) {
                    super.error(s)
                    Toast.makeText(this@NativeVideoActivity, "Oops! $s", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showTheRequestedAd() {
        if (responseId.isEmpty()) {
            Toast.makeText(this, "ResponseId is empty. Request an ad first", Toast.LENGTH_SHORT)
                .show()
            return
        }
        TapsellPlus.showNativeVideo(this, responseId, TapsellPlusVideoAdHolder.Builder()
            .setContentViewTemplate(R.layout.native_vid_template)
            .setAppInstallationViewTemplate(ir.tapsell.sdk.R.layout.tapsell_app_installation_video_ad_template)
            .setAdContainer(findViewById(R.id.adContainer))
            .build(), object : AdShowListener() {
            override fun onOpened(tapsellPlusAdModel: TapsellPlusAdModel) {
                super.onOpened(tapsellPlusAdModel)
                Toast.makeText(
                    this@NativeVideoActivity,
                    "Native Video is opened",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(tapsellPlusErrorModel: TapsellPlusErrorModel) {
                super.onError(tapsellPlusErrorModel)
                Toast.makeText(
                    this@NativeVideoActivity,
                    "Oops! " + tapsellPlusErrorModel.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun removeAd() {
        val nativeVideoAdContainer = findViewById<ViewGroup>(R.id.adContainer)
        if (nativeVideoAdContainer == null) {
            Toast.makeText(this, "View does not exist", Toast.LENGTH_SHORT).show()
            return
        }
        nativeVideoAdContainer.removeAllViews()
    }

}