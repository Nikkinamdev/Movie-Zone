package com.example.moviezone.app.ui.auth

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController


import com.example.moviezone.R
import com.example.moviezone.app.ui.HomeActivity
import com.example.moviezone.app.viewmodel.UserViewModel
import com.example.moviezone.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var session: SessionManager
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: UserViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        session = SessionManager(requireContext())
        setupLiveValidation()

        binding.loginBtn.setOnClickListener {

            val email = binding.emailEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString().trim()

            var isValid = true

            if (email.isEmpty()) {
                setInvalid(binding.emailLayout, "Email is required")
                isValid = false

            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                setInvalid(binding.emailLayout, "Enter valid email")
                isValid = false

            } else {
                setValid(binding.emailLayout)
            }

            if (password.isEmpty()) {
                setInvalid(binding.passwordLayout, "Password is required")
                isValid = false

            } else if (password.length < 6) {
                setInvalid(binding.passwordLayout, "Min 6 characters")
                isValid = false

            } else {
                setValid(binding.passwordLayout)
            }


            if (isValid) {
                viewModel.login(email)
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { user ->

            if (user != null) {
                session.setLogin(true)
                startActivity(Intent(requireContext(), HomeActivity::class.java))

                requireActivity().finish()

            } else {
                setInvalid(binding.emailLayout, "User not found")
            }
        }

        binding.signupBtn.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.forgotBtn.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_loginFragment_to_forgotPwdFragment)
        }
    }


    private fun setupLiveValidation() {

        binding.emailEdt.addTextChangedListener { text ->

            val email = text.toString().trim()

            if (email.isEmpty()) {
                setInvalid(binding.emailLayout, "Email is required")

            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                setInvalid(binding.emailLayout, "Enter valid email")

            } else {
                setValid(binding.emailLayout)
            }
        }

        binding.passwordEdt.addTextChangedListener { text ->

            val pass = text.toString()

            if (pass.isEmpty()) {
                setInvalid(binding.passwordLayout, "Password is required")

            } else if (pass.length < 6) {
                setInvalid(binding.passwordLayout, "Min 6 characters")

            } else {
                setValid(binding.passwordLayout)
            }
        }
    }


    private fun setValid(layout: TextInputLayout) {

        layout.isErrorEnabled = false
        layout.error = null
        layout.errorIconDrawable = null

        layout.boxStrokeColor =
            ContextCompat.getColor(requireContext(), R.color.green)
    }

    private fun setInvalid(layout: TextInputLayout, msg: String) {

        layout.isErrorEnabled = true
        layout.error = msg
        layout.errorIconDrawable = null

        layout.boxStrokeColor =
            ContextCompat.getColor(requireContext(), R.color.red)
    }
}