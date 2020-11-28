package com.ash.admincontrol.helper

import android.content.Context
import android.widget.Toast

class Toast
{
    companion object
    {
        fun show(context: Context,msg: String)
        {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

}