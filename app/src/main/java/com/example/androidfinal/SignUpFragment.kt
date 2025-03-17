package com.example.androidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class SignUpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editEmail = view.findViewById<EditText>(R.id.editEmail)
        val editPassword = view.findViewById<EditText>(R.id.editPassword)
        val buttonSignUp = view.findViewById<Button>(R.id.buttonSignUp)

        buttonSignUp.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(requireContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signUp_to_home)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
