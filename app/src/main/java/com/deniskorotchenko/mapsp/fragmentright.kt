package com.deniskorotchenko.mapsp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_fragmentright.view.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
     * [fragmentright.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [fragmentright.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentright : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    val singleton = Singleton.instance

    interface onNextListener {
        fun onNext() : Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fragmentright, container, false)
        view.textView.text="Вы на нужном месте! \n Пройдено ${singleton.nowQuestion}/${QuestDataBase(this.activity).getNumberOfQuestions()}.\n Нажмите для продолжения."
        view.next.setOnClickListener{
            val db = QuestDataBase(this.activity).getNumberOfQuestions()
            if (singleton.nowQuestion == db){
                val intent = Intent(this.activity, Last::class.java)
                startActivity(intent)
            }else
                singleton.nowQuestion++
            val fTr = this.activity.fragmentManager.beginTransaction()
            val onNextListenerVal = activity as onNextListener
            onNextListenerVal.onNext()

            fTr.remove(this)
            fTr.commit()
        }

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragmentright.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): fragmentright {
            val fragment = fragmentright()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
