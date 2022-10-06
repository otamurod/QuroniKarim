package com.otamurod.quronikarim.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.otamurod.quronikarim.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 5000

    //Hooks
    var second: View? = null
    var third: View? = null
    var fourth: View? = null
    var fifth: View? = null
    var sixth: View? = null
    var slogan: TextView? = null
    var a: TextView? = null

    //Animations
    var topAnimation: Animation? = null
    var bottomAnimation: Animation? = null
    var middleAnimation: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_splash)

        //Hooks
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        fourth = findViewById(R.id.fourth_line);
        fifth = findViewById(R.id.fifth_line);
        sixth = findViewById(R.id.sixth_line);
        a = findViewById(R.id.a);
        slogan = findViewById(R.id.tagLine);

        //Animation Calls
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation)

        //-----------Setting Animations to the elements of Splash

        second!!.animation = topAnimation
        third!!.animation = topAnimation
        fourth!!.animation = topAnimation
        fifth!!.animation = topAnimation
        sixth!!.animation = topAnimation
        a!!.animation = middleAnimation
        slogan!!.animation = bottomAnimation

        //Splash Screen Code to call new Activity after some time
        val handler = Handler().postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent);
                finish()
            }
        }, SPLASH_TIME_OUT.toLong())


    }
}
