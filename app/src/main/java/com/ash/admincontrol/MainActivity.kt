package com.ash.admincontrol

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ash.admincontrol.fragments.*
import com.ash.admincontrol.helper.Snackbar
import com.ash.admincontrol.networking.Event
import com.ash.admincontrol.networking.NetworkConnectivityListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity(), NetworkConnectivityListener
{
    private lateinit var noInternetLayout : TextView
    companion object
    {
        var firebaseDatabaseRefAllProducts = FirebaseDatabase.getInstance().reference.child("AllProducts")
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setTheme(R.style.mainAdminControl)
        setContentView(R.layout.activity_main)
        initLayout()
        initNavigationView()
    }

    private fun initLayout()
    {
         noInternetLayout = findViewById(R.id.addFragment_noInternetLayout)
    }


    private fun initNavigationView()
    {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)

        //Init it to use snackbar above Navigation view
        Snackbar.bottomBar = bottomNavigationView

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.bottomBarHome -> transToFragment(HomeFragment())
                R.id.bottomBarAccepted -> transToFragment(AcceptedFragment())
                R.id.bottomBarCanceled -> transToFragment(CancelledFragment())
                R.id.bottomBarDispatched -> transToFragment(DispatchFragment())
                R.id.bottomBarAdd -> transToFragment(AddFragment())
                else -> false
            }

        }
        //Default
        bottomNavigationView.selectedItemId = R.id.bottomBarHome
    }

    private fun transToFragment(fragment: Fragment) :Boolean
    {
        val fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun networkConnectivityChanged(event: Event)
    {
        when (event) {
            is Event.ConnectivityEvent ->
            {
                if (event.isConnected)
                {
                    noInternetLayout.setBackgroundResource(R.color.superlightgreen)
                    noInternetLayout.text = "Back Online!"
                    Handler().postDelayed({


                        val aniFade: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
                        noInternetLayout.startAnimation(aniFade)
                        noInternetLayout.visibility = View.GONE

                        //  viewGoneAnimator(noInternetLayout)
                    }, 2000)

                } else
                {
                    noInternetLayout.visibility = View.VISIBLE
                    noInternetLayout.setBackgroundResource(R.color.red)
                    noInternetLayout.text = "No Internet!"
                }
            }
        }
    }

    private fun viewGoneAnimator(view: View)
    {
        view.animate().alpha(0f).setDuration(500).setListener(object : AnimatorListenerAdapter()
        {
            override fun onAnimationEnd(animation: Animator)
            {
                view.visibility = View.INVISIBLE
            }
        })
    }

    private fun viewVisibleAnimator(view: View)
    {
        view.animate().alpha(1f).setDuration(500).setListener(object : AnimatorListenerAdapter()
        {
            override fun onAnimationEnd(animation: Animator)
            {
                view.visibility = View.VISIBLE
            }
        })
    }
}