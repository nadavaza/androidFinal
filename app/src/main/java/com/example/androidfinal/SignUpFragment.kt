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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.androidfinal.entities.User
import com.example.androidfinal.viewModel.UsersViewModel

class SignUpFragment : Fragment() {

    private val usersViewModel: UsersViewModel by activityViewModels()

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
        val textHaveAccount =
            view.findViewById<TextView>(R.id.textHaveAccount) // <-- Added reference

        buttonSignUp.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val name = editName.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(
                    email = email,
                    password = password,
                    name = name,
                )
                usersViewModel.registerUser(user)
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

        // Navigate to Sign In screen when clicking "Already have an account? Sign In"
        textHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signIn)
        }
    }
}
