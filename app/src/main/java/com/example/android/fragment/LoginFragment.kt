package com.example.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private var baseName: String = "Mohamed Magdy"
    private var basePassword: String = "m7madmagdy@app"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = binding.name
        val password = binding.password

        binding.loginButton.setOnClickListener {
            if (name.text.toString().isEmpty() || password.text.toString()
                    .isEmpty()
            ) {
                name.error = "Enter Your Name \uD83D\uDE02"
                password.error = "Enter Your Password \uD83D\uDC40"

            } else if (name.text.toString() != baseName || password.text.toString() != basePassword) {

                Toast.makeText(context, "Error Login, Try Again \uD83D\uDE35", Toast.LENGTH_SHORT).show()

            } else if (name.text.toString()
                    .isNotEmpty() && password.text.toString()
                    .isNotEmpty()
            )
                if (name.text.toString() == baseName && password.text.toString() == basePassword) {

                    Toast.makeText(context, "Login Successfully \uD83D\uDE0D", Toast.LENGTH_LONG).show()

                    val action = LoginFragmentDirections.actionLoginFragmentToListFragment(
                        baseName
                    )
                    findNavController().navigate(action)
                }
        }
    }
}