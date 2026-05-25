package com.example.moviezone.app.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.moviezone.R
import com.example.moviezone.app.viewmodel.UserViewModel
import com.example.moviezone.databinding.FragmentSignUpBinding
import com.google.android.material.textfield.TextInputLayout

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignUpBinding.bind(view)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setupLiveValidation()

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.AlredyaccBtn.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.signupBtn.setOnClickListener {

            val fullName = binding.fullNameEdt.text.toString().trim()
            val email = binding.emailEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString().trim()

            var isValid = true

            if (fullName.isEmpty()) {
                setInvalid(binding.fullNameLayout, "Name is required")
                isValid = false
            } else if (fullName.length < 3) {
                setInvalid(binding.fullNameLayout, "Min 3 characters")
                isValid = false
            } else {
                setValid(binding.fullNameLayout)
            }

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
                showLoader()
                viewModel.register(fullName, email)
            }
        }

        viewModel.registerResult.observe(viewLifecycleOwner) { message ->

            hideLoader()

            when (message) {

                "Registered Successfully" -> {
                    Toast.makeText(requireContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show()
                    requireView().findNavController()
                        .navigate(R.id.action_signUpFragment_to_loginFragment)
                }

                "User already exists" -> {
                    setInvalid(binding.emailLayout, "Email already registered")
                }

                else -> {
                    setInvalid(binding.emailLayout, "Something went wrong")
                }
            }
        }
    }


    private fun showLoader() {
        binding.signupBtn.isEnabled = false
        binding.signupLoader.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.signupBtn.isEnabled = true
        binding.signupLoader.visibility = View.GONE
    }


    private fun setupLiveValidation() {

        binding.fullNameEdt.addTextChangedListener {
            val value = it.toString().trim()

            if (value.isEmpty()) {
                setInvalid(binding.fullNameLayout, "Name is required")
            } else if (value.length < 3) {
                setInvalid(binding.fullNameLayout, "Min 3 characters")
            } else {
                setValid(binding.fullNameLayout)
            }
        }

        binding.emailEdt.addTextChangedListener {
            val value = it.toString().trim()

            if (value.isEmpty()) {
                setInvalid(binding.emailLayout, "Email is required")
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                setInvalid(binding.emailLayout, "Enter valid email")
            } else {
                setValid(binding.emailLayout)
            }
        }

        binding.passwordEdt.addTextChangedListener {
            val value = it.toString()

            if (value.isEmpty()) {
                setInvalid(binding.passwordLayout, "Password is required")
            } else if (value.length < 6) {
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

    private fun setInvalid(layout: TextInputLayout, message: String) {
        layout.isErrorEnabled = true
        layout.errorIconDrawable = null
        layout.error = message
        layout.boxStrokeColor =
            ContextCompat.getColor(requireContext(), R.color.red)
    }
}