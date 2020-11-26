package com.ash.admincontrol.helper

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.ash.admincontrol.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class Snackbar
{
    companion object
    {
        var bottomBar : BottomNavigationView? = null
        fun show(view: View, msg: String, res: Int)
        {
            val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            val sbView: View = snackbar.view

            sbView.setBackgroundColor(ContextCompat.getColor(view.context, res))

            val textView = sbView.findViewById(R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            snackbar.anchorView = bottomBar
            snackbar.setAction("Action", null)
            snackbar.show()

        }

    }



}