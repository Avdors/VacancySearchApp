package com.volkov.auth.presentation

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.volkov.auth.databinding.FragmentLoginSecondBinding
import com.volkov.core.utils.SessionManager
import kotlinx.coroutines.launch


class LoginSecondFragment : Fragment() {
    lateinit var binding: FragmentLoginSecondBinding
    private val viewModel: LoginViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginSecondBinding.inflate(inflater, container, false)

        setupEditTexts()

        // Наблюдаем за валидностью кода
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isCodeСompleted.collect { isComplete ->
                    binding.continueBt.isEnabled = isComplete
                    binding.continueBt.alpha = if (isComplete) 1.0f else 0.5f
                }
            }
        }

        // Кнопка продолжить
        binding.continueBt.alpha = 0.5f
        binding.continueBt.setOnClickListener {
            val fullCode = binding.et1.text.toString() + binding.et2.text.toString() +
                    binding.et3.text.toString() + binding.et4.text.toString()

            viewModel.validateCode(fullCode)

            if (viewModel.isCodeValid.value) {
                SessionManager.isLoggedIn = true
                val deepLinkUri = Uri.parse("app://vacancy.com/listVacancies")
                findNavController().navigate(deepLinkUri)
            } else {
                binding.errorKod.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Восстанавливаю состояние полей, если есть сохраненное состояние
        savedInstanceState?.let {
            binding.et1.setText(it.getString("et1_text"))
            binding.et2.setText(it.getString("et2_text"))
            binding.et3.setText(it.getString("et3_text"))
            binding.et4.setText(it.getString("et4_text"))
        }
    }

    private fun setupEditTexts() {
        val codeTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Собираем код из всех полей
                val fullCode = binding.et1.text.toString() + binding.et2.text.toString() +
                        binding.et3.text.toString() + binding.et4.text.toString()

                // Передаем код в ViewModel для проверки его заполненности
                viewModel.codeCompleted(fullCode)

                // Переход между полями при вводе символов
                when {
                    binding.et1.hasFocus() && binding.et1.text.length == 1 -> binding.et2.requestFocus()
                    binding.et2.hasFocus() && binding.et2.text.length == 1 -> binding.et3.requestFocus()
                    binding.et3.hasFocus() && binding.et3.text.length == 1 -> binding.et4.requestFocus()
                    binding.et4.hasFocus() && binding.et4.text.isEmpty() -> binding.et3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.et1.addTextChangedListener(codeTextWatcher)
        binding.et2.addTextChangedListener(codeTextWatcher)
        binding.et3.addTextChangedListener(codeTextWatcher)
        binding.et4.addTextChangedListener(codeTextWatcher)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("et1_text", binding.et1.text.toString())
        outState.putString("et2_text", binding.et2.text.toString())
        outState.putString("et3_text", binding.et3.text.toString())
        outState.putString("et4_text", binding.et4.text.toString())

    }

}