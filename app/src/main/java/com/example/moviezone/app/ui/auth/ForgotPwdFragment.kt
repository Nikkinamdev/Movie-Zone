package com.example.moviezone.app.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.moviezone.R
import com.example.moviezone.app.viewmodel.UserViewModel
import com.example.moviezone.databinding.FragmentForgotPwdBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPwdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPwdFragment : Fragment(R.layout.fragment_forgot_pwd) {
    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentForgotPwdBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel= ViewModelProvider(this)[UserViewModel::class.java]
        binding= FragmentForgotPwdBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        binding.backToLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_forgotPwdFragment_to_loginFragment)
        }
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()


        }



        binding.resetBtn.setOnClickListener {

            val email = binding.emailEdt.text.toString().trim()

            // Validation
            if (email.isEmpty()) {
                setInvalid(binding.emailLayout, "Email is required")
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                setInvalid(binding.emailLayout, "Enter valid email")
                return@setOnClickListener
            }

            setValid(binding.emailLayout)
            binding.progressBar.visibility = View.VISIBLE
            binding.resetBtn.isEnabled = false
            lifecycleScope.launch {

                val user = viewModel.checkEmail(email)

                if (user != null) {

                    // ✅ SAME EMAIL FOUND
                    Log.d("MAillllllllllllllllll","${user}")
                    Toast.makeText(
                        requireContext(),
                        "Reset link sent successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.resetBtn.isEnabled = true
                    // ❌ EMAIL NOT FOUND
                    setInvalid(binding.emailLayout, "User not found")
                }
            }
        }
        setupLiveValidation()
    }
    private fun setupLiveValidation() {



        // EMAIL
        binding.emailEdt.addTextChangedListener {
            val text = it.toString().trim()
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                setValid(binding.emailLayout)
            } else {
                setInvalid(binding.emailLayout, "Enter valid email")
            }
        }


    }

    private fun setValid(layout: TextInputLayout) {
        layout.isErrorEnabled = false
        layout.error = null

        layout.boxStrokeColor =
            ContextCompat.getColor(requireContext(), R.color.green)
    }

    private fun setInvalid(layout: TextInputLayout, message: String) {

        layout.isErrorEnabled = true
        layout.error = message
        layout.errorIconDrawable = null
        layout.boxStrokeColor =
            ContextCompat.getColor(requireContext(), R.color.red)
    }
}