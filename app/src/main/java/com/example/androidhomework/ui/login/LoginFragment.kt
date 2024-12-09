package com.example.androidhomework.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.androidhomework.R
import kotlin.text.isBlank
import kotlin.text.isNotEmpty

class LoginFragment : Fragment() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginEditText = view.findViewById(R.id.loginEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton)

        loginButton.isEnabled = false

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                loginButton.isEnabled =
                    loginEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            }
        }

        loginEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        loginButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (login.isBlank() || password.isBlank()) {
                return@setOnClickListener
            } else {
                val action = LoginFragmentDirections.actionLoginFragmentToNotesFragment(login)
                findNavController().navigate(action)

                Toast.makeText(requireContext(), getString(R.string.LoginToast), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}