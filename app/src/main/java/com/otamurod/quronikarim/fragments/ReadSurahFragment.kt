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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.otamurod.quronikarim.R
import com.otamurod.quronikarim.databinding.FragmentReadSurahBinding
import com.otamurod.quronikarim.models.Surahs
import com.otamurod.quronikarim.models.SurahsPageNumbers


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 */
class ReadSurahFragment : Fragment() {
    private var position: Int? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var getActivity: AppCompatActivity
    private lateinit var readSurahBinding: FragmentReadSurahBinding
    private lateinit var pdfView: PDFView
    private lateinit var mAdView: AdView

    @SuppressLint("ResourceType", "SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        readSurahBinding = FragmentReadSurahBinding.inflate(layoutInflater, container, false)

        getActivity = (activity as AppCompatActivity?)!!
        getActivity.supportActionBar!!.show() //show appbar/toolbar
        getActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)//set visible
        getActivity.supportActionBar!!.setHomeButtonEnabled(true)
        getActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back) //set navigation icon

        val surahs = Surahs()
        val listOfSurahs = surahs.getListOfSurahs()

        if (arguments != null) {
            position = arguments?.getInt("position") as Int
        }
        getActivity.supportActionBar!!.title = listOfSurahs[position!!] //change toolbar title

        pdfView = readSurahBinding.pdfView

        readSurahBinding.pdfView.maxZoom = 3f
        readSurahBinding.pdfView.midZoom = 2f
        readSurahBinding.pdfView.minZoom = 1f

        val surahsPageNumbers = SurahsPageNumbers()
        val pageNumbers = surahsPageNumbers.getPageNumbers()
        showPdfFromAssets(getPdfNameFromAssets(), pageNumbers[position!!])


        MobileAds.initialize(requireContext()) {}

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("3C290B96642177F4331BF7BCDB3B5C99"))
                .build()
        )

        mAdView = readSurahBinding.adView

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

        return readSurahBinding.root
    }

    private fun getPdfNameFromAssets(): String {
        return "full.pdf"
    }

    private fun showPdfFromAssets(pdfName: String, pageNumber: Int) {
        pdfView.fromAsset(pdfName)
            .password(null) // if password protected, then write password
            .defaultPage(pageNumber)// set the default page to open
            .onPageError { page, _ ->
                Toast.makeText(
                    context,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }

    override fun onPause() {
        super.onPause()
        mAdView.pause()
    }

    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

}