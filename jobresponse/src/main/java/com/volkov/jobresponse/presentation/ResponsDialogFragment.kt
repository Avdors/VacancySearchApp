package com.volkov.jobresponse.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.volkov.jobresponse.databinding.FragmentResponsDialogBinding


class ResponsDialogFragment : BottomSheetDialogFragment() {
    private var binding: FragmentResponsDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResponsDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Получаем аргумент questionText
        val questionText = arguments?.getString("questionText")

        binding?.addCoverLetterText?.setOnClickListener { view ->
            view.visibility = View.GONE
            binding?.letterEt?.visibility = View.VISIBLE

        }

        binding?.applyButton?.setOnClickListener {
            dismiss()
        }


        // Если questionText передан, то делаем поле видимым и подставляем текст
        if (!questionText.isNullOrEmpty()) {
            binding?.letterEt?.visibility = View.VISIBLE
            binding?.addCoverLetterText?.visibility = View.GONE
            binding?.letterEt?.setText(questionText)
        } else {
            // Если вопрос не передан, прячем поле
            binding?.letterEt?.visibility = View.GONE
            binding?.addCoverLetterText?.visibility = View.VISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}