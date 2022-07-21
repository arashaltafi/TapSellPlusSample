package com.arash.altafi.tapsellplussample

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arash.altafi.tapsellplussample.databinding.ActivityVastBinding
import com.google.ads.interactivemedia.v3.api.AdErrorEvent
import com.google.ads.interactivemedia.v3.api.AdErrorEvent.AdErrorListener
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.ads.interactivemedia.v3.api.AdEvent.AdEventListener
import com.google.ads.interactivemedia.v3.api.AdEvent.AdEventType
import com.google.ads.interactivemedia.v3.api.CompanionAdSlot
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import ir.tapsell.plus.TapsellPlus

class VastActivity : AppCompatActivity(), AdEventListener, AdErrorListener {

    private lateinit var binding: ActivityVastBinding
    private val TAG = "VastActivity"
    private var player: SimpleExoPlayer? = null
    private var adsLoader: ImaAdsLoader? = null
    private var sdkFactory: ImaSdkFactory? = null
    private var companionAdSlots: ArrayList<CompanionAdSlot>? = null
    private var isShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            btnRequest.setOnClickListener {
                if (!isShowing) {
                    isShowing = true
                    prepareVideo()
                } else {
                    Toast.makeText(this@VastActivity, "wait for complete ALL ADS", Toast.LENGTH_SHORT).show()
                }
            }

            sdkFactory = ImaSdkFactory.getInstance()
            val settings = sdkFactory?.createImaSdkSettings()
            settings?.language = "fa"

            addCompanionAds()

            // Create an AdsLoader.
            adsLoader = ImaAdsLoader.Builder(this@VastActivity)
                .setImaSdkSettings(settings!!)
                .setAdEventListener(this@VastActivity)
                .setAdErrorListener(this@VastActivity)
                .setCompanionAdSlots(companionAdSlots!!)
                .build()
        }
    }

    private fun addCompanionAds() {
        binding.apply {
            companionAdSlot.visibility = View.VISIBLE
            val companionAdSlot1 = sdkFactory!!.createCompanionAdSlot()
            companionAdSlot1.container = companionAdSlot
            companionAdSlot1.setSize(320, 50)
            companionAdSlots = java.util.ArrayList()
            companionAdSlots?.add(companionAdSlot1)
        }
    }

    private fun prepareVideo() {
        // Create the MediaItem to play, specifying the content URI and ad tag URI.
        val tag = TapsellPlus.getVastTag(BuildConfig.TAPSELL_VAST_PREROLL)
        val contentUri = Uri.parse("https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4")
        val adTagUri = Uri.parse(tag)
        val mediaItem = MediaItem.Builder().setUri(contentUri).setAdTagUri(adTagUri).build()
        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player?.setMediaItem(mediaItem)
        player?.prepare()
    }

    private fun initializePlayer() {
        binding.apply {
            // Set up the factory for media sources, passing the ads loader and ad view providers.
            val dataSourceFactory: DataSource.Factory =
                DefaultDataSourceFactory(this@VastActivity, Util.getUserAgent(this@VastActivity, getString(R.string.app_name)))
            val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
                .setAdsLoaderProvider { adsLoader }
                .setAdViewProvider(playerView)

            // Create a SimpleExoPlayer and set it as the player for content and ads.
            player = SimpleExoPlayer.Builder(this@VastActivity).setMediaSourceFactory(mediaSourceFactory).build()
            playerView.player = player
            adsLoader?.setPlayer(player)

            // Set PlayWhenReady. If true, content and ads will autoplay.
            player?.playWhenReady = true
        }
    }

    private fun releasePlayer() {
        adsLoader?.setPlayer(null)
        binding.playerView.player = null
        player?.release()
        player = null
    }

    override fun onAdEvent(event: AdEvent?) {
        binding.apply {
            when (event!!.type) {
                AdEventType.STARTED -> companionAdSlot.visibility = View.VISIBLE
                AdEventType.AD_PROGRESS -> {}
                AdEventType.COMPLETED, AdEventType.SKIPPED -> {}
                AdEventType.ALL_ADS_COMPLETED -> {
                    isShowing = false
                    companionAdSlot.visibility = View.GONE
                    tvLog.append(event.type.name.trimIndent())
                }
                else -> tvLog.append(event.type.name.trimIndent())
            }
        }
    }

    override fun onAdError(event: AdErrorEvent?) {
        binding.apply {
            tvLog.append(event?.error?.message + "\n")
            Log.e(TAG, "Ad Error: " + event?.error?.message)
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
            binding.playerView.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
            binding.playerView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            binding.playerView.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            binding.playerView.onPause()
            releasePlayer()
        }
    }

    override fun onDestroy() {
        adsLoader!!.release()
        super.onDestroy()
    }

}