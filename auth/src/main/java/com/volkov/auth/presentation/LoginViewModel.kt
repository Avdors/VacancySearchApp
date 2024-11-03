package com.volkov.auth.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {    // Email и его валидность
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _isEmailValid = MutableStateFlow(false)
    val isEmailValid: StateFlow<Boolean> = _isEmailValid

    // Код и его валидность
    private val _code = MutableStateFlow("")
    val code: StateFlow<String> = _code

    private val _isCodeValid = MutableStateFlow(false)
    val isCodeValid: StateFlow<Boolean> = _isCodeValid

    private val _isCodeСompleted = MutableStateFlow(false)
    val isCodeСompleted: StateFlow<Boolean> = _isCodeСompleted

    // Сгенерированный код и уведомление
    private val _generatedCode = MutableStateFlow("")
    private val _codeNotification = MutableSharedFlow<String>() // SharedFlow для уведомлений
    val codeNotification: SharedFlow<String> = _codeNotification

    // Проверка email
    fun validateEmail(email: String) {
        _email.value = email
        _isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Генерация кода
    fun generateCode() {
        val randomCode = (0..9).shuffled().take(4).joinToString("")
        _generatedCode.value = randomCode
        viewModelScope.launch {
            _codeNotification.emit("Ваш код: $randomCode")
        }
    }

    // Проверка введенного кода
    fun validateCode(inputCode: String) {
        _isCodeValid.value = _generatedCode.value == inputCode
    }


    fun codeCompleted(inputCode: String) {
        _isCodeСompleted.value = inputCode.length == 4

    }

    // Обновление кода
    fun updateCode(index: Int, char: Char) {
        val currentCode = _code.value.toMutableList()
        if (index < currentCode.size) {
            currentCode[index] = char
        } else {
            currentCode.add(char)
        }
        _code.value = currentCode.joinToString("")
        validateCode(_code.value)
    }

}