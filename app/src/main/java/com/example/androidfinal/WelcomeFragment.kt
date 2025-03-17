package com.example.androidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class WelcomeFragment : Fragment() {
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

        buttonSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_signIn)
        }

        buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_signUp)
        }
    }
}
