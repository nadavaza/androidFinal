package com.example.androidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.androidfinal.viewModel.UsersViewModel

class WelcomeFragment : Fragment() {

    private val usersViewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonSignIn = view.findViewById<Button>(R.id.buttonSignIn)
        val buttonSignUp = view.findViewById<Button>(R.id.buttonSignUp)

        usersViewModel.getUser()

        usersViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                findNavController().navigate(R.id.action_welcome_to_home)
            }
        }

        buttonSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_signIn)
        }

        buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_signUp)
        }
    }
}
