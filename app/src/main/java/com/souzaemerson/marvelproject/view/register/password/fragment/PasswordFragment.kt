package com.souzaemerson.marvelproject.view.register.password.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.data.model.User
import com.souzaemerson.marvelproject.databinding.FragmentPasswordBinding
import com.souzaemerson.marvelproject.util.setError
import com.souzaemerson.marvelproject.view.register.password.viewmodel.PasswordViewModel

class PasswordFragment : Fragment() {

    private lateinit var binding: FragmentPasswordBinding
    private lateinit var viewModel: PasswordViewModel
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = PasswordViewModel.PasswordViewModelProvideFactory()
            .create(PasswordViewModel::class.java)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User

        observeVMEvents()
        goToPhotoStep()
        backToPreviousStep()
    }

    private fun goToPhotoStep() {
        binding.nextButton.setOnClickListener {
            val password = binding.passwordEdit.text.toString()
            val passwordConfirmation = binding.confirmPasswordEdit.text.toString()

            if (viewModel.checkIfPasswordAreValid(password, passwordConfirmation)) {
                navigateToNextStepWithValidPassword(password)
            }
        }
    }

    private fun navigateToNextStepWithValidPassword(password: String) {
        val userWithNewValues = User(user.email, user.name, password, null)

        findNavController().navigate(R.id.action_passwordFragment_to_photoFragment,
            Bundle().apply {
                putParcelable("REGISTER_USER", userWithNewValues)
            })
    }

    private fun backToPreviousStep(){
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeVMEvents() {
        viewModel.passwordFieldErrorResId.observe(viewLifecycleOwner) {
            binding.passwordLayout.setError(requireContext(), it)
        }
        viewModel.passwordIsDifferentFieldErrorResId.observe(viewLifecycleOwner) {
            binding.confirmPasswordLayout.setError(requireContext(), it)
        }
    }
}