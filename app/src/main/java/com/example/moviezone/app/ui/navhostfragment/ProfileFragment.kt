package com.example.moviezone.app.ui.navhostfragment


import SessionManager
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.moviezone.MainActivity
import com.example.moviezone.R
import com.example.moviezone.app.ui.auth.LoginFragment
import com.example.moviezone.app.viewmodel.UserViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var imageView: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var logoutBtn: TextView

    private lateinit var viewModel: UserViewModel
    private lateinit var session: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imgProfile)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        logoutBtn = view.findViewById(R.id.logout)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        viewModel.loadUser()

        session = SessionManager(requireContext())

        viewModel.userData.observe(viewLifecycleOwner) { user ->

            if (user != null) {
                tvName.text = user.name
                tvEmail.text = user.email
            }
        }
        logoutBtn.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->

                    viewModel.logout()
                    session.logout()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                    requireActivity().finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
        tvName.setOnClickListener {
            showEditDialog("Edit Name") { newName ->

                viewModel.updateName(newName, tvEmail.text.toString())
            }
        }

        tvEmail.setOnClickListener {
            showEditDialog("Edit Email") { newEmail ->

                val oldEmail = tvEmail.text.toString()
                viewModel.updateEmail(oldEmail, newEmail)
            }
        }

        imageView.setOnClickListener {
            showImageOptions()
        }
    }


    private fun showEditDialog(title: String, onSave: (String) -> Unit) {

        val editText = EditText(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                onSave(editText.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showImageOptions() {

        val options = arrayOf("Camera", "Gallery")

        AlertDialog.Builder(requireContext())
            .setTitle("Choose Image")
            .setItems(options) { _, which ->

                when (which) {
                    0 -> checkCameraPermission()
                    1 -> galleryLauncher.launch("image/*")
                }
            }
            .show()
    }

    private val galleryLauncher =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {
                imageView.setImageURI(uri)
            }
        }

    private val cameraLauncher =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            }
        }


    private val cameraPermissionLauncher =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {
                cameraLauncher.launch(null)
            }
        }

    private fun checkCameraPermission() {

        val permission = android.Manifest.permission.CAMERA

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(null)
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }
}