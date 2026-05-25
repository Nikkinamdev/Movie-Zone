package com.example.moviezone.app.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moviezone.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.moviezone.app.viewmodel.UserViewModel
import com.example.moviezone.databinding.FragmentEditprofileBinding

class EditProfileFragment : Fragment(R.layout.fragment_editprofile) {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var binding: FragmentEditprofileBinding

    private lateinit var viewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentEditprofileBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        btnSave = view.findViewById(R.id.btnSave)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        viewModel.loadUser()

        viewModel.userData.observe(viewLifecycleOwner) { user ->

            if (user != null) {

                etName.setText(user.name)
                etEmail.setText(user.email)
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.navigationIcon?.setTint(
            ContextCompat.getColor(requireContext(), android.R.color.white)
        )
        btnSave.setOnClickListener {

            val updatedName = etName.text.toString().trim()

            if (updatedName.isEmpty()) {

                etName.error = "Name cannot be empty"
                etName.requestFocus()

                Toast.makeText(requireContext(), "Please enter name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.updateName(updatedName, etEmail.text.toString())

            view.findNavController().popBackStack()
        }
    }
}