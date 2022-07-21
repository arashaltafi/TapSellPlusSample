package com.arash.altafi.tapsellplussample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.tapsellplussample.databinding.ActivityMainBinding
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTapSell()
        init()
    }

    private fun initTapSell() {
        TapsellPlus.initialize(this, BuildConfig.TAPSELL_KEY, object : TapsellPlusInitListener {
            override fun onInitializeSuccess(adNetworks: AdNetworks) {
                Log.d("onInitializeSuccess", adNetworks.name)
            }

            override fun onInitializeFailed(
                adNetworks: AdNetworks,
                adNetworkError: AdNetworkError
            ) {
                Log.e("onInitializeFailed", "ad network: " + adNetworks.name + ", error: " + adNetworkError.errorMessage)
            }
        })

        TapsellPlus.setGDPRConsent(this, true)
        TapsellPlus.setDebugMode(Log.DEBUG)
    }

    private fun init() {
        binding.apply {
            btRewardedVideo.setOnClickListener {
                startActivity(Intent(this@MainActivity, RewardedVideoActivity::class.java))
            }
            btInterstitial.setOnClickListener {
                startActivity(Intent(this@MainActivity, InterstitialActivity::class.java))
            }
            btNativeBanner.setOnClickListener {
                startActivity(Intent(this@MainActivity, NativeBannerActivity::class.java))
            }
            btStandardBanner.setOnClickListener {
                startActivity(Intent(this@MainActivity, StandardBannerActivity::class.java))
            }
            btNativeBannerInList.setOnClickListener {
                startActivity(Intent(this@MainActivity, NativeBannerInListActivity::class.java))
            }
            btNativeVideo.setOnClickListener {
                startActivity(Intent(this@MainActivity, NativeVideoActivity::class.java))
            }
            btVast.setOnClickListener {
                startActivity(Intent(this@MainActivity, VastActivity::class.java))
            }
        }
    }

}