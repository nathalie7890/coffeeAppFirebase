package com.nathalie.coffeeapp.ui.presentation.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.databinding.FragmentSignupBinding
import com.nathalie.coffeeapp.ui.presentation.user.viewmodels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnSignup.setOnClickListener {
                val name = etUsername.text.toString()
                val email = etEmail.text.toString()
                val pass = etPassword.text.toString()
                val confirmPass = etPassword2.text.toString()

                viewModel.signUp(name, email, pass, confirmPass)
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                val action = LoginFragmentDirections.toLoginFragment()
                NavHostFragment.findNavController(this@SignupFragment).navigate(action)
            }
        }
    }
}