package com.deniskorotchenko.mapsp


import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.opengl.Visibility
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.fragment_question.view.*
import kotlinx.android.synthetic.main.fragment_tip.view.*


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
        view.closeQuestionFragment.setOnClickListener {
            close()
        }
        view.tipFragmentText.text = QuestDataBase(this.activity as Context).getTip(singleton.nowQuestion)
        if (needTip == false){
            view.tipFragmentText.visibility = View.GONE
            view.textTipZag.visibility = View.GONE
        }
        return view
    }

    companion object {
        var needTip : Boolean = true
        fun newInstance(needTip : Boolean): QuestionFragment {
            QuestionFragment.needTip = needTip
            val view = QuestionFragment()
            return view
        }
    }

}// Required empty public constructor
