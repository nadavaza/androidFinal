package com.example.androidfinal

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.androidfinal.entities.User
import com.example.androidfinal.viewModel.UsersViewModel

class SignUpFragment : Fragment() {

    private val usersViewModel: UsersViewModel by activityViewModels()
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editEmail = view.findViewById<EditText>(R.id.editEmail)
        val editName = view.findViewById<EditText>(R.id.editName)
        val editPassword = view.findViewById<EditText>(R.id.editPassword)
        val buttonSignUp = view.findViewById<Button>(R.id.buttonSignUp)
        val buttonChooseProfileImage = view.findViewById<Button>(R.id.buttonChooseProfileImage)
        val profileImageView = view.findViewById<ImageView>(R.id.profileImageView)
        val textHaveAccount = view.findViewById<TextView>(R.id.textHaveAccount)

        // Handle selecting a profile image
        buttonChooseProfileImage.setOnClickListener {
            openGallery()
        }

        // Handle sign-up logic
        buttonSignUp.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val name = editName.text.toString().trim()
            var selectedBitmap: Bitmap? = null
            profileImageView.isDrawingCacheEnabled = true
            profileImageView.buildDrawingCache()
            selectedBitmap = (profileImageView.drawable as? BitmapDrawable)?.bitmap

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(
                    id = "", // Provide a default empty string or generate a unique ID
                    email = email,
                    password = password,
                    name = name,
                    photo = "", // Provide an empty string or a default profile picture URL
                    lastUpdated = System.currentTimeMillis()
                )

                usersViewModel.registerUser(user, selectedBitmap)
                usersViewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
                    if (currentUser != null) {
                        findNavController().navigate(R.id.action_signUp_to_home)
                    } else {
                        Toast.makeText(requireContext(), "Sign Up failed!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        textHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signIn)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data!!.data
                val profileImageView = view?.findViewById<ImageView>(R.id.profileImageView)
                profileImageView?.setImageResource(android.R.drawable.ic_menu_gallery)
                profileImageView?.visibility = View.VISIBLE
            }
        }
}
