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

package com.otamurod.quronikarim.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.otamurod.quronikarim.R
import com.otamurod.quronikarim.databinding.ActivityMainBinding
import com.otamurod.quronikarim.databinding.MenuLayoutBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menuLayoutBinding: MenuLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        supportActionBar!!.hide()

    }

    //set click listener to navigation button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.about -> {
                showAlertDialog()
            }
        }
        /** return value matters. If true returned, cannot be overwritten in fragment
         * So return false if you want to override */
        return false
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        // Set the message show for the Alert time
        menuLayoutBinding = MenuLayoutBinding.inflate(layoutInflater)

        menuLayoutBinding.developerContact.setOnClickListener {
            val developerEmail = menuLayoutBinding.developerContact.text.toString()

            val toast = Toast.makeText(this, getString(R.string.contact_developer), Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()

            val email = Intent(Intent.ACTION_SEND)
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf(developerEmail))
            //need this to prompts email client only
            email.type = "message/rfc822"

            startActivity(Intent.createChooser(email, "Send Email"))

        }
        builder.setView(menuLayoutBinding.root)

        // Create the Alert dialog
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}