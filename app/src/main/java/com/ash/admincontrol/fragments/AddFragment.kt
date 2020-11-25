package com.ash.admincontrol.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView

import androidx.fragment.app.Fragment
import com.ash.admincontrol.R


class AddFragment : Fragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {

        val view: View = inflater.inflate(R.layout.fragment_add, container, false)
        val searchView = view.findViewById<SearchView>(R.id.addFragment_Searchbar)
        val textViewTitle = view.findViewById<TextView>(R.id.addFragment_textviewTitle)


        initSearchView(searchView,textViewTitle)


        return view
    }

    private fun initSearchView(searchView: SearchView, textViewTitle:TextView)
    {
        val title = textViewTitle.text

        searchView.setOnClickListener {
            searchView.isIconified = false
        }

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if(hasFocus)
            {
                textViewTitle.text = ""
            } else
            {
                textViewTitle.text = title
            }
        }
    }


}