package com.volkov.auth.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.volkov.auth.R
import com.volkov.auth.databinding.FragmentLoginFirstBinding
import kotlinx.coroutines.launch


class LoginFirstFragment : Fragment() {
    private val viewModel: LoginViewModel by activityViewModels()
    lateinit var binding: FragmentLoginFirstBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isEmailValid.collect() { isValid ->
                    binding.emailContinueBt.isEnabled = isValid
                    binding.emailContinueBt.alpha = if (isValid) 1.0f else 0.5f
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.codeNotification.collect() { notification ->
                    Toast.makeText(requireContext(), notification, Toast.LENGTH_LONG).show()
                }
            }
        }


        //обработка ввода email
        binding.emailEt.doOnTextChanged { text, start, before, count ->
            val email = text.toString()
            viewModel.validateEmail(email)
        }

        // Кнопка продолжить
        binding.emailContinueBt.setOnClickListener {
            viewModel.generateCode()
            // Здесь будет проверенный email
            val deepLinkUri =
                Uri.parse("app://vacancy.com/loginSecondFragment?email=${viewModel.email.value}")
            findNavController().navigate(deepLinkUri)

        }

        binding.emailClearImage.setOnClickListener {
            binding.emailEt.text.clear()
        }
    }

    // Метод для отправки уведомления
    private fun sendNotification(code: String) {
        // Создаем Intent для перехода к LoginSecondFragment по Deep Link
        val deepLinkUri =
            Uri.parse("app://vacancy.com/loginSecondFragment?email=${viewModel.email.value}")
        val notificationIntent = Intent(Intent.ACTION_VIEW, deepLinkUri)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создание канала уведомлений для Android 8.0 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "code_channel_id",
                "Verification Code",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(requireContext(), "code_channel_id")
            .setSmallIcon(R.drawable.responses_icon) // Ваша иконка для уведомления
            .setContentTitle("Verification Code")
            .setContentText("Ваш код: $code")
            .setContentIntent(pendingIntent) // Intent для перехода по уведомлению
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}