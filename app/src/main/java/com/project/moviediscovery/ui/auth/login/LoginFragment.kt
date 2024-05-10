package com.project.moviediscovery.ui.auth.login

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import com.project.moviediscovery.utils.Result
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.project.moviediscovery.R
import com.project.moviediscovery.databinding.FragmentLoginBinding
import com.project.moviediscovery.ui.auth.AuthViewModel
import com.project.moviediscovery.ui.auth.register.RegisterFragment
import com.project.moviediscovery.ui.main.MainActivity
import com.project.moviediscovery.utils.Constants.alertDialogMessage
import com.project.moviediscovery.utils.UserPreferences
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loadingDialog: AlertDialog

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val inflaterLoad: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(requireContext())
            .setView(inflaterLoad.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(true)
        loadingDialog = loadingAlert.create()

        setupPlayAnimation()
        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (isValid()) {
                    authViewModel.loginUser(
                        binding.edEmail.text.toString(),
                        binding.edPassword.text.toString()
                    ).observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                loadingDialog.show()
                            }

                            is Result.Success -> {
                                authViewModel.savePreferences(
                                    true,
                                    result.data?.email.toString(),
                                    UserPreferences.preferenceDefaultValue
                                )
                                loadingDialog.dismiss()

                                requireActivity().apply {
                                    requireActivity().startActivity(
                                        Intent(
                                            this,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            }

                            is Result.Error -> {
                                loadingDialog.dismiss()
                                alertDialogMessage(requireContext(), "Gagal Autentikasi User!")
                            }
                        }
                    }
                }
            }

            btnRegister.setOnClickListener { changeToRegister() }
        }
    }

    private fun isValid() = if (binding.edEmail.text.isNullOrEmpty()
    ) {
        alertDialogMessage(requireContext(), "Masukkan email dengan benar!")
        false
    } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmail.text.toString())
            .matches() || binding.edEmail.text.isNullOrEmpty()
    ) {
        alertDialogMessage(requireContext(), "Masukkan format email dengan benar!")
        false
    } else if (binding.edPassword.text.isNullOrEmpty()) {
        alertDialogMessage(requireContext(), "Masukkan password dengan benar!")
        false
    } else {
        true
    }


    private fun changeToRegister() {
        parentFragmentManager.beginTransaction().apply {
            replace(
                R.id.auth_container,
                RegisterFragment(),
                RegisterFragment::class.java.simpleName
            )
            commit()
        }
    }

    @SuppressLint("Recycle")
    private fun setupPlayAnimation() {
        val title: Animator =
            ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(150)
        val email: Animator =
            ObjectAnimator.ofFloat(binding.edEmailLayout, View.ALPHA, 1f).setDuration(150)
        val password: Animator =
            ObjectAnimator.ofFloat(binding.edPasswordLayout, View.ALPHA, 1f).setDuration(150)
        val button: Animator =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(150)
        val layoutText: Animator =
            ObjectAnimator.ofFloat(binding.layoutText, View.ALPHA, 1f).setDuration(150)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(title, email, password, button, layoutText)
        animatorSet.startDelay = 150
        animatorSet.start()
    }
}