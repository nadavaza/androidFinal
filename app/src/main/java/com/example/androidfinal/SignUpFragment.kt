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
import com.example.androidfinal.AddPostFragment.Companion
import com.example.androidfinal.AddPostFragment.Companion.PICK_IMAGE_REQUEST
import com.example.androidfinal.entities.User
import com.example.androidfinal.viewModel.UsersViewModel

class SignUpFragment : Fragment() {

    private val usersViewModel: UsersViewModel by activityViewModels()
    private lateinit var editEmail: EditText
    private lateinit var editName: EditText
    private lateinit var editPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var buttonChooseProfileImage: Button
    private lateinit var profileImageView: ImageView
    private lateinit var textHaveAccount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editEmail = view.findViewById(R.id.editEmail)
        editName = view.findViewById(R.id.editName)
        editPassword = view.findViewById(R.id.editPassword)
        buttonSignUp = view.findViewById(R.id.buttonSignUp)
        buttonChooseProfileImage = view.findViewById(R.id.buttonChooseProfileImage)
        profileImageView = view.findViewById(R.id.profileImageView)
        textHaveAccount = view.findViewById(R.id.textHaveAccount)

        buttonChooseProfileImage.setOnClickListener {
            openGallery()
        }

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
                    id = "",
                    email = email,
                    password = password,
                    name = name,
                    photo = "",
                    lastUpdated = System.currentTimeMillis()
                )

                usersViewModel.registerUser(user, selectedBitmap)
                usersViewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
                    if (currentUser != null) {
                        findNavController().navigate(R.id.action_signUp_to_home)
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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddPostFragment.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            profileImageView.setImageURI(data.data)
            profileImageView.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 100
    }
}
