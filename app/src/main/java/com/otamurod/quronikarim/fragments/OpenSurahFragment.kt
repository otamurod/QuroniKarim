/**
 * BISMILLAHIR ROHMANIR ROHIYM
 *
 * PROGRAM NAME: QUR'ONI KARIM
 * AUTHOR: SAFAROV OTAMUROD YUSUFALI O'G'LI
 * FINISHED ON: 08.08.2022, at 08:00 a.m
 *
 * DESCRIPTION: Tinglovchilar Muborak Qur'oni Karim suralari qiroatlarini to'liq eshitib bahramand bo'lish, shuningdek, o'zbek tilidagi tarjimasini o'qishlari mumkin. Alloh barchamizni o'zi iymonimizni mustahkam qilsin! Ushbu ilova orqali musulmonlarga foydam tegishidan umidvorman. Barcha insonlarga Allohning roziligini qo'lga kiritish nasib qilsin!
 *
 */

package com.otamurod.quronikarim.fragments

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.otamurod.quronikarim.R
import com.otamurod.quronikarim.databinding.FragmentOpenSurahBinding
import com.otamurod.quronikarim.models.Surahs
import com.otamurod.quronikarim.models.SurahsAudios


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 */
class OpenSurahFragment : Fragment(), MediaPlayer.OnPreparedListener {
    private var position: Int? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)

        if (arguments != null) {
            position = arguments?.getInt("position") as Int
        }

    }

    private lateinit var getActivity: AppCompatActivity
    lateinit var openSurahBinding: FragmentOpenSurahBinding

    lateinit var handler: Handler
    private var mediaPlayer: MediaPlayer? = null
    private var endTime: String? = null
    private var currentTime: String? = null
    private var audioDuration: Int = 0

    private val surahsAudios = SurahsAudios()
    private val audiosOfSurahs = surahsAudios.getAudiosOfSurahs()
    private val surahs = Surahs()
    private val listOfSurahs = surahs.getListOfSurahs()
    private lateinit var audioURL: String

    private lateinit var mAdView: AdView

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        openSurahBinding = FragmentOpenSurahBinding.inflate(layoutInflater, container, false)

        getActivity = (activity as AppCompatActivity?)!!
        getActivity.supportActionBar!!.show() //show appbar/toolbar
        getActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)//set visible
        getActivity.supportActionBar!!.setHomeButtonEnabled(true)
        getActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back) //set navigation icon

        setInfo(position)

        openSurahBinding.nextBtn.setOnClickListener {
            if (position!! < 113) {
                releaseMP()
                position = position!! + 1
                audioURL = audiosOfSurahs[position!!]
                setInfo(position)
            }
        }
        openSurahBinding.previousBtn.setOnClickListener {
            if (position!! > 0) {
                releaseMP()
                position = position!! - 1
                audioURL = audiosOfSurahs[position!!]
                setInfo(position)
            }
        }

        handler = Handler(Looper.getMainLooper())
        audioURL = audiosOfSurahs[position!!]

        openSurahBinding.playBtn.setOnClickListener {

            if (mediaPlayer == null) {
                openSurahBinding.playBtn.setImageResource(R.drawable.ic_pause)
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setDataSource(audioURL)
                mediaPlayer!!.setOnPreparedListener(this)
                mediaPlayer!!.prepareAsync()
                openSurahBinding.playBtn.isClickable = false
            } else if (mediaPlayer?.isPlaying!!) {
                openSurahBinding.playBtn.setImageResource(R.drawable.ic_play_arrow)
                mediaPlayer?.pause()
            } else if (!mediaPlayer?.isPlaying!!) {
                openSurahBinding.playBtn.setImageResource(R.drawable.ic_pause)
                mediaPlayer?.start()
            }
        }

        openSurahBinding.readBtn.setOnClickListener {
            val bundle = bundleOf("position" to position)
            findNavController().navigate(R.id.readSurahFragment, bundle)
        }

        openSurahBinding.seekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    if (mediaPlayer != null) {
                        mediaPlayer?.seekTo(openSurahBinding.seekbar.progress)
                        returnCurrentTime(mediaPlayer?.currentPosition)
                    } else {
                        openSurahBinding.seekbar.progress = 0
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


        MobileAds.initialize(requireContext()) {}

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("3C290B96642177F4331BF7BCDB3B5C99"))
                .build()
        )

        mAdView = openSurahBinding.adView

        /*mAdView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                super.onAdFailedToLoad(adError)
                val toastMessage = "ad fail to load"
//                val toastMessage: String = adError.toString()
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()

                // Gets the domain from which the adError came.
                val errorDomain = adError.domain
                // Gets the adError code. See
                // https://developers.google.com/android/reference/com/google/android/gms/ads/AdRequest#constant-summary
                // for a list of possible codes.
                val adErrorCode = adError.code
                // Gets an adError message.
                // For example "Account not approved yet". See
                // https://support.google.com/admob/answer/9905175 for explanations of
                // common adErrors.
                val adErrorMessage = adError.message
                // Gets additional response information about the request. See
                // https://developers.google.com/admob/android/response-info for more
                // information.
                val responseInfo = adError.responseInfo
                // Gets the cause of the adError, if available.
                val cause = adError.cause
                // All of this information is available via the adError's toString() method.
                Log.d("Ads", adError.toString())
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                val toastMessage = "ad loaded"
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            }

            override fun onAdOpened() {
                super.onAdOpened()
                val toastMessage = "ad is open"
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                val toastMessage = "ad is clicked"
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                val toastMessage = "ad is closed"
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                val toastMessage = "ad impression"
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            }
        }*/

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        return openSurahBinding.root
    }


    private fun resetPlayer() {
        openSurahBinding.playBtn.setImageResource(R.drawable.ic_play_arrow)
        currentTime = "00:00:00"
        endTime = "/00:00:00"
        openSurahBinding.currentTime.text = currentTime
        openSurahBinding.totalTime.text = endTime

        openSurahBinding.seekbar.progress = 0

    }

    private fun setInfo(position: Int?) {
        openSurahBinding.surah.text = listOfSurahs[position!!]
        getActivity.supportActionBar!!.title = listOfSurahs[position]

        val url =
            "@drawable/number_${position + 1}" // where number (without the extension) is the file
        val imageResource: Int = resources.getIdentifier(url, null, requireContext().packageName)

        Glide.with(requireContext()).load(imageResource)
            .into(openSurahBinding.surahNumber)

        resetPlayer()

    }

    private fun returnCurrentTime(currentPosition: Int?) {
        audioDuration = currentPosition!! / 1000

        val hour = (audioDuration / 3600)
        val min = (audioDuration % 3600) / 60
        val sec = (audioDuration % 60)

        when {
            hour <= 0 -> {
                if (min < 10) {
                    currentTime = if (sec < 10) {
                        "00:0$min:0$sec"
                    } else {
                        "00:0$min:$sec"
                    }
                }
                if (min >= 10) {
                    currentTime = if (sec < 10) {
                        "00:$min:0$sec"
                    } else {
                        "00:$min:$sec"
                    }
                }
            }

            hour in 1..9 -> {

                if (min < 10) {
                    currentTime = if (sec < 10) {
                        "0$hour:0$min:0$sec"
                    } else {
                        "0$hour:0$min:$sec"
                    }
                }
                if (min >= 10) {
                    currentTime = if (sec < 10) {
                        "0$hour:$min:0$sec"
                    } else {
                        "0$hour:$min:$sec"
                    }
                }
            }

            hour in 10..25 -> {

                if (min < 10) {
                    currentTime = if (sec < 10) {
                        "$hour:0$min:0$sec"
                    } else {
                        "$hour:0$min:$sec"
                    }
                }
                if (min >= 10) {
                    currentTime = if (sec < 10) {
                        "$hour:$min:0$sec"
                    } else {
                        "$hour:$min:$sec"
                    }
                }
            }

        }
    }

    private fun returnEndTime() {
        audioDuration = mediaPlayer!!.duration / 1000

        val hourEnd = (audioDuration / 3600)
        val minEnd = (audioDuration % 3600) / 60
        val secEnd = (audioDuration % 60)

        when {
            hourEnd <= 0 -> {
                if (minEnd < 10) {
                    endTime = if (secEnd < 10) {
                        "/00:0$minEnd:0$secEnd"
                    } else {
                        "/00:0$minEnd:$secEnd"
                    }
                }
                if (minEnd >= 10) {
                    endTime = if (secEnd < 10) {
                        "/00:$minEnd:0$secEnd"
                    } else {
                        "/00:$minEnd:$secEnd"
                    }
                }
            }

            hourEnd in 1..9 -> {

                if (minEnd < 10) {
                    endTime = if (secEnd < 10) {
                        "/0$hourEnd:0$minEnd:0$secEnd"
                    } else {
                        "/0$hourEnd:0$minEnd:$secEnd"
                    }
                }
                if (minEnd >= 10) {
                    endTime = if (secEnd < 10) {
                        "/0$hourEnd:$minEnd:0$secEnd"
                    } else {
                        "/0$hourEnd:$minEnd:$secEnd"
                    }
                }
            }

            hourEnd in 10..25 -> {

                if (minEnd < 10) {
                    endTime = if (secEnd < 10) {
                        "/$hourEnd:0$minEnd:0$secEnd"
                    } else {
                        "/$hourEnd:0$minEnd:$secEnd"
                    }
                }
                if (minEnd >= 10) {
                    endTime = if (secEnd < 10) {
                        "/$hourEnd:$minEnd:0$secEnd"
                    } else {
                        "/$hourEnd:$minEnd:$secEnd"
                    }
                }
            }
        }
    }

    private var runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null) {
                returnCurrentTime(mediaPlayer?.currentPosition!!)
            }
            val currentPosition = if (mediaPlayer != null) mediaPlayer?.currentPosition!! else 0

            if (mediaPlayer != null && !mediaPlayer?.isPlaying!!) {
                openSurahBinding.playBtn.setImageResource(R.drawable.ic_play_arrow) //set play icon if music finished
            } else if (mediaPlayer != null && mediaPlayer?.isPlaying!!) {
                openSurahBinding.playBtn.setImageResource(R.drawable.ic_pause)
            }
            /** working */
            if (mediaPlayer != null) {
                openSurahBinding.currentTime.text = currentTime
                openSurahBinding.seekbar.max = mediaPlayer?.duration!!
                openSurahBinding.seekbar.progress = currentPosition
            }
            /**  Play Next Song If player finished*/
            if (mediaPlayer != null && currentTime == endTime) {
                openSurahBinding.playBtn.setImageResource(R.drawable.ic_play_arrow) //set play icon if music finished
                mediaPlayer!!.stop()
            }

            handler.postDelayed(this, 100)
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        returnEndTime()
        openSurahBinding.totalTime.text = endTime
        mediaPlayer!!.start()
        openSurahBinding.playBtn.isClickable = true

        handler.postDelayed(runnable, 100)
    }

    private fun releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mAdView.pause()
    }

    override fun onResume() {
        super.onResume()

        setInfo(position)

        if (mediaPlayer != null) {
            returnEndTime()
            openSurahBinding.totalTime.text = endTime
            mediaPlayer!!.start()
        }

        mAdView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMP()
    }

}