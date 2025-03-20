package com.example.androidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.androidfinal.viewModel.UsersViewModel

class SignInFragment : Fragment() {

    private val usersViewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInTitle = view.findViewById<TextView>(R.id.signInTitle) // ✅ Added Title
        val editEmail = view.findViewById<EditText>(R.id.editEmail)
        val editPassword = view.findViewById<EditText>(R.id.editPassword)
        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)
        val textNoAccount = view.findViewById<TextView>(R.id.textNoAccount)

        buttonLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                usersViewModel.login(email, password)
                usersViewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
                    if (currentUser != null) {
                        findNavController().navigate(R.id.action_signIn_to_home)
                    } else {
                        Toast.makeText(requireContext(), "Login failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ Navigate to Sign Up page when clicked
        textNoAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_signUp)
        }
    }
}
