package com.deniskorotchenko.mapsp

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_false_answer.view.*

class FalseAnswerFragment : Fragment() {

    val singleton = Singleton.instance


    interface FalseAnswerListener {
        fun onTip()
        fun onBackFromAnswer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_false_answer, container, false)
        view.buttonHide.setOnClickListener {
            val falseAnswerListener = activity as FalseAnswerListener
            falseAnswerListener.onTip()
            destroyThis()
        }
        view.buttonBack.setOnClickListener{
            destroyThis()
        }
        return view
    }

    private fun destroyThis(){
        val falseAnswerListener = activity as FalseAnswerListener
        val fTr = this.activity.fragmentManager.beginTransaction()
        fTr.remove(this)
        falseAnswerListener.onBackFromAnswer()
        fTr.commit()
    }

    companion object {

        fun newInstance(): FalseAnswerFragment {
            val fragment = FalseAnswerFragment()
            return fragment
        }
    }
}// Required empty public constructor
