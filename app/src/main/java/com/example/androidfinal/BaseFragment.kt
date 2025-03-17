package com.example.androidfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

open class BaseFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}