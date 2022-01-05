package com.example.android.fragment

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.Gravity.apply
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private var baseName: String = "Mohamed Magdy"
    private var basePassword: String = "m7madmagdy@app"

    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser("Authentication Error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                notifyUser("Login Successfully")
                val action = LoginFragmentDirections.actionLoginFragmentToListFragment(baseName)
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

    @RequiresApi(Build.VERSION_CODES.Q)
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
                Toast.makeText(context, "Error Login, Try Again \uD83D\uDE35", Toast.LENGTH_SHORT)
                    .show()
            } else if (name.text.toString()
                    .isNotEmpty() && password.text.toString()
                    .isNotEmpty()
            )
                if (name.text.toString() == baseName && password.text.toString() == basePassword) {

                    Toast.makeText(context, "Login Successfully \uD83D\uDE0D", Toast.LENGTH_LONG)
                        .show()
                    val action = LoginFragmentDirections.actionLoginFragmentToListFragment(
                        baseName
                    )
                    findNavController().navigate(action)
                }
        }

        checkBiometricSupport()
        // create a biometric dialog on Click of button
        binding.fingerPrint.setOnClickListener {
            // This creates a dialog of biometric auth and
            // it requires title , subtitle ,
            // and description
            // In our case there is a cancel button by
            // clicking it, it will cancel the process of
            // fingerprint authentication
            val biometricPrompt = BiometricPrompt.Builder(requireActivity())
                .setTitle("Title of Prompt")
                .setSubtitle("Subtitle")
                .setDescription("Uses FP")
                .setNegativeButton(
                    "Cancel",
                    requireActivity().mainExecutor,
                    DialogInterface.OnClickListener { dialog, which ->
                        notifyUser("Authentication Cancelled")
                    }).build()

            // start the authenticationCallback in mainExecutor
            biometricPrompt.authenticate(
                getCancellationSignal(),
                requireActivity().mainExecutor,
                authenticationCallback
            )
        }
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was Cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    // it checks whether the app the app has fingerprint permission
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager =
            requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint Authentication Permission is not enabled")
            return false
        }
        return if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    private fun notifyUser(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}