package com.deniskorotchenko.mapsp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fragmentright.view.*

class FalseAnswerFragment : Fragment() {

    val singleton = Singleton.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_false_answer, container, false)

        return view
    }

    companion object {

        fun newInstance(): FalseAnswerFragment {
            val fragment = FalseAnswerFragment()
            return fragment
        }
    }
}// Required empty public constructor
