package com.ash.admincontrol


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ash.admincontrol.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setTheme(R.style.mainAdminControl)
        setContentView(R.layout.activity_main)

        initNavigationView()
    }

    private fun initNavigationView()
    {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)

        bottomNavigationView.setOnNavigationItemSelectedListener {

            when(it.itemId)
            {
                R.id.bottomBarHome ->
                {
                    val fragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                    transaction.replace(R.id.frame_layout, HomeFragment())
                    transaction.commit()
                    true
                }
                R.id.bottomBarAccepted ->
                {

                    val fragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                    transaction.replace(R.id.frame_layout, AcceptedFragment())
                    transaction.commit()
                    true
                }
                R.id.bottomBarCanceled ->
                {

                    val fragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                    transaction.replace(R.id.frame_layout, CancelledFragment())
                    transaction.commit()
                    true
                }
                R.id.bottomBarDispatched ->
                {

                    val fragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                    transaction.replace(R.id.frame_layout, DispatchFragment())
                    transaction.commit()
                    true
                }
                R.id.bottomBarAdd ->
                {
                    val fragmentManager = supportFragmentManager
                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                    transaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                    transaction.replace(R.id.frame_layout, AddFragment())
                    transaction.commit()
                    true
                }


                else -> false
            }

        }

        bottomNavigationView.selectedItemId = R.id.bottomBarHome
    }
}