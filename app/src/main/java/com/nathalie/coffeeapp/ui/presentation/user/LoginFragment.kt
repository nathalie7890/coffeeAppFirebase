package com.nathalie.coffeeapp.ui.presentation.user


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.nathalie.coffeeapp.MainActivity
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.databinding.FragmentLoginBinding
import com.nathalie.coffeeapp.ui.presentation.user.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
// Fragment bound to the Login UI
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            // login the user
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val pass = etPassword.text.toString()
                viewModel.login(email, pass)
            }
        }

        // Navigates to the SignUp fragment
        binding.btnSignup.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToSignup()
            NavHostFragment.findNavController(this).navigate(action)
        }

        //launch after login
        lifecycleScope.launch {
            viewModel.loginFinish.collect {
                (activity as MainActivity).drawerUsername()

                val action = LoginFragmentDirections.toMainFragment()
                NavHostFragment.findNavController(this@LoginFragment).navigate(action)
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("finish_login", bundle)
            }
        }
    }
}