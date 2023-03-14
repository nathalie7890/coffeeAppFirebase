package com.nathalie.coffeeapp.ui.presentation.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.nathalie.coffeeapp.R
import com.nathalie.coffeeapp.databinding.FragmentSignupBinding
import com.nathalie.coffeeapp.ui.presentation.user.viewmodels.LoginViewModel
import com.nathalie.coffeeapp.ui.presentation.user.viewmodels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
// Fragment bound to the SignUp UI
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: SignupViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var email: String
    private lateinit var pass: String

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
            // registers the user
            btnSignup.setOnClickListener {
                val name = etUsername.text.toString()
                email = etEmail.text.toString()
                pass = etPassword.text.toString()
                val confirmPass = etPassword2.text.toString()

                viewModel.signUp(name, email, pass, confirmPass)
            }
        }

        //after sign up, login
        lifecycleScope.launch {
            viewModel.finish.collect {
                loginViewModel.login(email, pass)
            }
        }

        //after login
        lifecycleScope.launch {
            loginViewModel.loginFinish.collect {
                //fill firestore with drinks, beans and roasts
                viewModel.fillWithStartingDrinks(requireContext())
                viewModel.fillWithStartingBeans(requireContext())
                viewModel.fillWithStartingRoasts(requireContext())

                //navigate to main fragment
                val action = SignupFragmentDirections.toMainFragment()
                NavHostFragment.findNavController(this@SignupFragment).navigate(action)
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("finish_login", bundle)

            }
        }
    }
}