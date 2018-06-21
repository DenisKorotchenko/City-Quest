package com.deniskorotchenko.mapsp

import android.content.Intent
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fragmentright.view.*
import java.util.*

class AnswerFragment : Fragment() {

    private val singleton = Singleton.instance

    interface AnswerFragmentListener {
        fun onNext() : Unit
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fragmentright, container, false)
        view.textView.text="Вы на нужном месте! \n Пройдено ${singleton.nowQuestion}/${QuestDataBase(this.activity).getNumberOfQuestions()}.\n Нажмите для продолжения."
        var numberOfQuestions = QuestDataBase(this.activity).getNumberOfQuestions()
        view.next.setOnClickListener{
            numberOfQuestions = QuestDataBase(this.activity).getNumberOfQuestions()
            if (singleton.nowQuestion == numberOfQuestions){
                val intent = Intent(this.activity, Last::class.java)
                startActivity(intent)
            }else
                singleton.nowQuestion++
            val fTr = this.activity.fragmentManager.beginTransaction()
            val onNextListenerVal = activity as AnswerFragmentListener
            onNextListenerVal.onNext()
            fTr.remove(this)
            fTr.commit()
        }

        if (singleton.nowQuestion == numberOfQuestions){
            singleton.finishTime = Calendar.getInstance().timeInMillis
        }

        return view
    }

    companion object {

        fun newInstance(): AnswerFragment {
            return AnswerFragment()
        }
    }
}// Required empty public constructor
