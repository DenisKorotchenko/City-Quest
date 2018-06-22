package com.deniskorotchenko.mapsp


import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_question.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [QuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionFragment : Fragment(){
    var lastDownY = 0
    var lastUpY = 0

    private val singleton = Singleton.instance

    private fun close(){
        val fTr = this.activity.fragmentManager.beginTransaction()
        fTr.setCustomAnimations(R.animator.slide_in_bottom, R.animator.slide_to_bottom)
        fTr.remove(this)
        fTr.commit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        view.questionFragmentText.text = QuestDataBase(this.activity as Context).getQuestion(singleton.nowQuestion)
        return view
    }

    companion object {
        fun newInstance(): QuestionFragment {
            return QuestionFragment()
        }
    }

}// Required empty public constructor
