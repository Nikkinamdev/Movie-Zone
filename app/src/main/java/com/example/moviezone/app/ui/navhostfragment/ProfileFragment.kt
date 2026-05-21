package com.example.moviezone.app.ui.navhostfragment

import SessionManager
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.moviezone.MainActivity
import com.example.moviezone.R
import com.example.moviezone.app.viewmodel.HomeViewModel
import com.example.moviezone.app.viewmodel.UserViewModel
import java.io.File

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var imageView: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var logoutBtn: LinearLayout
    private lateinit var btnEdit: Button

    private lateinit var viewModel: UserViewModel
    private lateinit var viewModelhome: HomeViewModel
    private lateinit var session: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imgProfile)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        logoutBtn = view.findViewById(R.id.logout)
        btnEdit = view.findViewById(R.id.btnEdit)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModelhome = ViewModelProvider(this)[HomeViewModel::class.java]
        session = SessionManager(requireContext())

        loadUserData()

        btnEdit.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        logoutBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.logout()
                    viewModelhome.logoutfavmovieData()
                    session.logout()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        imageView.setOnClickListener {
            showImageOptions()
        }
    }

    private fun loadUserData() {

        viewModel.loadUser()

        viewModel.userData.observe(viewLifecycleOwner) { user ->

            if (user != null) {

                tvName.text = user.name
                tvEmail.text = user.email

                // ✅ LOAD IMAGE FROM DB
                if (!user.profileImageUri.isNullOrEmpty()) {
                    imageView.setImageURI(Uri.parse(user.profileImageUri))
                } else {
                    imageView.setImageResource(R.drawable.ic_user)
                }
            }
        }
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
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            if (uri != null) {
                imageView.setImageURI(uri)

                // SAVE TO DB
                viewModel.saveProfileImage(uri.toString())
            }
        }


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->

            if (bitmap != null) {

                imageView.setImageBitmap(bitmap)

                val uri = saveBitmapToInternalStorage(bitmap)
                viewModel.saveProfileImage(uri.toString())
            }
        }

    private fun saveBitmapToInternalStorage(bitmap: Bitmap): Uri {

        val file = File(
            requireContext().filesDir,
            "profile_${System.currentTimeMillis()}.png"
        )

        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        return Uri.fromFile(file)
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) {
                cameraLauncher.launch(null)
            }
        }

    private fun checkCameraPermission() {

        val permission = Manifest.permission.CAMERA

        if (ContextCompat.checkSelfPermission(requireContext(), permission)
            == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(null)
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }
}