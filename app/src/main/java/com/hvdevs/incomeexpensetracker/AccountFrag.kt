package com.hvdevs.incomeexpensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class AccountFrag : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)
        
        //creamos las listas para el list view
        val options: ArrayList<String> = arrayListOf(
            "Invite Friends",
            "Account Info",
            "Personal Profile",
            "Message Center",
            "Login and Security",
            "Data and Privacy"
        )
        val images: ArrayList<Int> = arrayListOf(
            R.mipmap.man,
            R.mipmap.man,
            R.mipmap.man,
            R.mipmap.man,
            R.mipmap.man,
            R.mipmap.man
        )

        val lv = view.findViewById<ListView>(R.id.lv)

        val programAdapter = context?.let { AccountListViewAdapter(it, options, images) }

        lv.adapter = programAdapter

        return view
    }
}