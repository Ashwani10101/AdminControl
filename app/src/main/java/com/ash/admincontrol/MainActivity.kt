package com.ash.admincontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ash.admincontrol.fragments.*
import com.ash.admincontrol.helper.Snackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity()
{

    companion object
    {
        var firebaseDatabaseRefAllProducts = FirebaseDatabase.getInstance().reference.child("AllProducts")
    }
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
}