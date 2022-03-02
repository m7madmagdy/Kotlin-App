package com.example.android.ui.login

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val baseName: String = "Kotlin App"
    private val basePassword: String = "app@kotlin"

    private var cancellationSignal: CancellationSignal? = null

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString!!)
                notify("Authentication Error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result!!)
                notify("Login Successfully \uD83D\uDE0D")
                val action = LoginFragmentDirections.actionLoginFragmentToListFragment()
                findNavController().navigate(action)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()

        /** hide actionBar **/
        val activity = (activity as AppCompatActivity?)!!
        activity.supportActionBar!!.hide()

        val name = binding.edtName
        val password = binding.edtPassword
        binding.loginButton.setOnClickListener {
            if (name.text.toString().isEmpty() || password.text.toString().isEmpty()
            ) {
                name.error = "Enter Your Name \uD83D\uDE02"
                password.error = "Enter Your Password \uD83D\uDC40"

            } else if (name.text.toString() != baseName || password.text.toString() != basePassword) {
                notify("Error Login, Try Again \uD83D\uDE35")
            } else if (name.text.toString().isNotEmpty() && password.text.toString().isNotEmpty())
                if (name.text.toString() == baseName && password.text.toString() == basePassword) {
                    notify("Login Successfully \uD83D\uDE0D")
                    val action = LoginFragmentDirections.actionLoginFragmentToListFragment()
                    findNavController().navigate(action)

                    // Save Login Data
                    val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("saveData", 0)
                    val editor: SharedPreferences.Editor? = sharedPreferences.edit()
                    editor?.putString("name", name.text.toString())
                    editor?.putString("pass", password.text.toString())
                    editor?.apply()
                }
        }

        checkBiometricSupport()
        binding.fingerPrintImg.setOnClickListener {
            val biometricPrompt = BiometricPrompt.Builder(requireActivity())
                .setTitle("Finger Print")
                .setSubtitle("Login With Your Finger")
                .setNegativeButton("Cancel", requireActivity().mainExecutor) { _, _ ->
                    notify("Authentication Cancelled")
                }.build()

            // start the authenticationCallback in mainExecutor
            biometricPrompt.authenticate(
                getCancellationSignal(),
                requireActivity().mainExecutor,
                authenticationCallback
            )
        }
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("saveData", 0)
        val name: String? = sharedPreferences.getString("name", "")
        val pass: String? = sharedPreferences.getString("pass", "")
        binding.edtName.setText(name)
        binding.edtPassword.setText(pass)
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notify("Authentication was Cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    // it checks whether the app the app has fingerprint permission
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager =
            requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            notify("Fingerprint authentication has not been enabled in settings")
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notify("Fingerprint Authentication Permission is not enabled")
            return false
        }
        return if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    private fun notify(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}
