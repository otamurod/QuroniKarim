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

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.otamurod.quronikarim.adapters.SurahAdapter
import com.otamurod.quronikarim.databinding.FragmentSurahBinding
import com.otamurod.quronikarim.models.Surahs


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 */
class SurahFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var surahBinding: FragmentSurahBinding
    private lateinit var getActivity: AppCompatActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        getActivity = (activity as AppCompatActivity?)!!

        getActivity.supportActionBar!!.show() //show appbar/toolbar
        getActivity.supportActionBar!!.title = "QUR'ONI KARIM"
        getActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(false)//set visible
        getActivity.supportActionBar!!.setHomeButtonEnabled(false)

        // Inflate the layout for this fragment
        surahBinding = FragmentSurahBinding.inflate(layoutInflater, container, false)

        setRVHeight()

        val surahs = Surahs()
        val listOfSurahs = surahs.getListOfSurahs()

        surahBinding.rvMain.adapter =
            SurahAdapter(requireContext(), listOfSurahs, object : SurahAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val bundle = bundleOf("position" to position)
                    findNavController().navigate(
                        com.otamurod.quronikarim.R.id.openSurahFragment,
                        bundle
                    )
                }

            })

        return surahBinding.root
    }

    private fun setRVHeight() {
        val displayMetrics = DisplayMetrics()
        getActivity.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        // Gets recycler view
        // Gets the layout params that will allow you to resize the layout
        val params: ViewGroup.LayoutParams = surahBinding.rvMain.layoutParams
        // Changes the height and width to the specified *pixels*
        params.height = height - 500
//        params.width = width
        surahBinding.rvMain.layoutParams = params
    }
}