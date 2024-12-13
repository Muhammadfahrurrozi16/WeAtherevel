package com.example.weatheravel

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Handler(Looper.getMainLooper()).postDelayed({

            findNavController().navigate(R.id.action_splashScreenFragment_to_rekomendasiPage)
        }, 3000) // 3000 ms = 3 detik
    }

    companion object {

        fun newInstance(param1: String, param2: String): SplashScreenFragment {
            val fragment = SplashScreenFragment()
            val bundle = Bundle().apply {
                putString("param1", param1)
                putString("param2", param2)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
