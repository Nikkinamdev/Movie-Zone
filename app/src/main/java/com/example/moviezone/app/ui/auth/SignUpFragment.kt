package com.example.moviezone.app.ui.auth

import android.os.Bundle
import android.view.View
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

        // NAVIGATION
        binding.AlredyaccBtn.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        // SIGNUP BUTTON
        binding.signupBtn.setOnClickListener {

            val fullName = binding.fullNameEdt.text.toString().trim()
            val email = binding.emailEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString().trim()

            var isValid = true

            // FULL NAME
            if (fullName.isEmpty()) {
                setInvalid(binding.fullNameLayout, "Name is required")
                isValid = false

            } else if (fullName.length < 3) {
                setInvalid(binding.fullNameLayout, "Min 3 characters")
                isValid = false

            } else {
                setValid(binding.fullNameLayout)
            }

            // EMAIL
            if (email.isEmpty()) {
                setInvalid(binding.emailLayout, "Email is required")
                isValid = false

            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                setInvalid(binding.emailLayout, "Enter valid email")
                isValid = false

            } else {
                setValid(binding.emailLayout)
            }

            // PASSWORD
            if (password.isEmpty()) {
                setInvalid(binding.passwordLayout, "Password is required")
                isValid = false

            } else if (password.length < 6) {
                setInvalid(binding.passwordLayout, "Min 6 characters")
                isValid = false

            } else {
                setValid(binding.passwordLayout)
            }

            // SAVE TO ROOM
            if (isValid) {
                viewModel.register(fullName, email)
            }
        }

        // OBSERVE RESULT
        viewModel.registerResult.observe(viewLifecycleOwner) { message ->

            if (message == "Registered Successfully") {

                requireView().findNavController()
                    .navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
    }

    // 🔥 LIVE VALIDATION
    private fun setupLiveValidation() {

        // FULL NAME
        binding.fullNameEdt.addTextChangedListener { text ->

            val value = text.toString().trim()

            if (value.isEmpty()) {
                setInvalid(binding.fullNameLayout, "Name is required")

            } else if (value.length < 3) {
                setInvalid(binding.fullNameLayout, "Min 3 characters")

            } else {
                setValid(binding.fullNameLayout)
            }
        }

        // EMAIL
        binding.emailEdt.addTextChangedListener { text ->

            val value = text.toString().trim()

            if (value.isEmpty()) {
                setInvalid(binding.emailLayout, "Email is required")

            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                setInvalid(binding.emailLayout, "Enter valid email")

            } else {
                setValid(binding.emailLayout)
            }
        }

        // PASSWORD
        binding.passwordEdt.addTextChangedListener { text ->

            val value = text.toString()

            if (value.isEmpty()) {
                setInvalid(binding.passwordLayout, "Password is required")

            } else if (value.length < 6) {
                setInvalid(binding.passwordLayout, "Min 6 characters")

            } else {
                setValid(binding.passwordLayout)
            }
        }
    }

    // ✅ VALID (GREEN BORDER + NO ICON)
    private fun setValid(layout: TextInputLayout) {

        layout.isErrorEnabled = false
        layout.error = null
        layout.errorIconDrawable = null

        layout.boxStrokeColor =
            ContextCompat.getColor(requireContext(), R.color.green)
    }

    // ❌ INVALID (RED BORDER + NO ICON)
    private fun setInvalid(layout: TextInputLayout, message: String) {

        layout.isErrorEnabled = true
        layout.error = message
        layout.errorIconDrawable = null

        layout.boxStrokeColor =
            ContextCompat.getColor(requireContext(), R.color.red)
    }
}