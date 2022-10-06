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

package com.otamurod.quronikarim.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.otamurod.quronikarim.databinding.TicketSurahBinding


class SurahAdapter(
    var context: Context,
    private var list: List<String>,
    var onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<SurahAdapter.VH>() {

    inner class VH(private var surahBinding: TicketSurahBinding) :
        RecyclerView.ViewHolder(surahBinding.root) {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("UseCompatLoadingForDrawables")
        fun onBind(string: String, position: Int) {

            surahBinding.surahName.text = string

            val uri = "@drawable/number_${position+1}" // where myresource (without the extension) is the file
            val imageResource: Int = context.resources.getIdentifier(uri, null, context.packageName)

            Glide.with(itemView).load(imageResource)
                .into(surahBinding.surahNumber)

            surahBinding.ticket.setOnClickListener {
                onItemClickListener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(TicketSurahBinding.inflate(LayoutInflater.from(parent.context)))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemClick(string: Int)
    }

}
