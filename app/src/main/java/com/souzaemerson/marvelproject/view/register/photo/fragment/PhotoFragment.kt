package com.souzaemerson.marvelproject.view.register.photo.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.core.Status
import com.souzaemerson.marvelproject.data.db.AppDatabase
import com.souzaemerson.marvelproject.data.db.daos.UserDAO
import com.souzaemerson.marvelproject.data.model.User
import com.souzaemerson.marvelproject.data.repository.register.RegisterRepository
import com.souzaemerson.marvelproject.data.repository.register.RegisterRepositoryImpl
import com.souzaemerson.marvelproject.databinding.FragmentPhotoBinding
import com.souzaemerson.marvelproject.view.register.photo.viewmodel.PhotoViewModel
import kotlinx.coroutines.Dispatchers


class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private lateinit var viewModel: PhotoViewModel
    private lateinit var repository: RegisterRepository
    private lateinit var user: User
    private var uriImage: Uri? = null
    private val dao: UserDAO by lazy {
        AppDatabase.getDb(requireContext()).userDao()
    }
    private var getContent = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        uri?.let {
            setImageFromGallery(it)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User

        repository = RegisterRepositoryImpl(dao)
        viewModel = PhotoViewModel.PhotoViewModelProvider(repository, Dispatchers.IO)
            .create(PhotoViewModel::class.java)

        clickToChoosePhoto()
        insertUserOnDatabase()
        createUserWithoutPhoto()
        observeVMEvents()
    }

    private fun clickToChoosePhoto() {
        binding.registerCardview.setOnClickListener {
            gallery()
        }
    }

    private fun gallery() = getContent.launch("image/*")

    private fun setImageFromGallery(uri: Uri){
            binding.registerImage.setImageURI(uri)
            uriImage = uri
    }

    private fun createUserWithoutPhoto() {
        binding.registerChooseLater.setOnClickListener {
            viewModel.insertNewUserOnDatabase(user)
        }
    }

    private fun insertUserOnDatabase() {
        binding.photoButtonConfirm.setOnClickListener {
            val finalUser = user.copy(photo = uriImage)
            viewModel.insertNewUserOnDatabase(finalUser)
        }
    }

    private fun observeVMEvents() {
        viewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        goToNextStepWithValidUser()
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao cadastrar usuário",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun goToNextStepWithValidUser() {
        findNavController().navigate(R.id.action_photoFragment_to_welcomeFragment,
            Bundle().apply {
                putParcelable("REGISTER_USER", user)
            })
    }
}