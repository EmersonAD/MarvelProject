package com.souzaemerson.marvelproject.view.favorite.fragment

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.core.Status
import com.souzaemerson.marvelproject.data.db.AppDatabase
import com.souzaemerson.marvelproject.data.db.daos.CharacterDAO
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepository
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.souzaemerson.marvelproject.data.model.Favorites
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.data.model.User
import com.souzaemerson.marvelproject.databinding.FragmentFavoriteBinding
import com.souzaemerson.marvelproject.util.ConfirmDialog
import com.souzaemerson.marvelproject.util.toast
import com.souzaemerson.marvelproject.view.adapter.CharacterAdapter
import com.souzaemerson.marvelproject.view.favorite.viewmodel.FavoriteViewModel
import com.souzaemerson.marvelproject.data.model.converterToResult
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class FavoriteFragment : Fragment() {
    lateinit var viewModel: FavoriteViewModel
    lateinit var repository: DatabaseRepository
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var user: User
    private lateinit var favoriteAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            user = it.intent.getParcelableExtra<User>("USER") as User
        }
        repository = DatabaseRepositoryImpl(dao)
        viewModel = FavoriteViewModel(repository, Dispatchers.IO)

        observeVMEvents()
    }

    private fun observeVMEvents() {
        viewModel.getCharacters(user.email).observe(viewLifecycleOwner) {
            when {
                it.isNotEmpty() -> {
                    Timber.tag("LISTARESULT").i(it.toString())
                    setRecycler(converterToResult(it))
                }
                else -> {
                    binding.favoriteContainer.setBackgroundResource(R.drawable.no_bg)
                    setRecycler(converterToResult(it))
                }
            }
        }
        viewModel.delete.observe(viewLifecycleOwner) { state ->
            when (state.status) {
                Status.SUCCESS -> {
                    state.data?.let {
                        toast("Personagem deletado")
                    }
                }
                Status.ERROR -> {
                    Timber.tag("error").i(state.error.toString())
                }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun setAdapter(characterList: List<Results>) {
        favoriteAdapter = CharacterAdapter(characterList, ::goToDetails, ::deleteCharacter)
    }

    private fun deleteCharacter(favorites: Favorites) {
        ConfirmDialog(
            getString(R.string.confirmation),
            getString(R.string.message_connection)
        ).apply {
            setListener {
                val newFavorite = favorites.copy(email = user.email)
                viewModel.deleteCharacter(newFavorite)
            }
        }.show(parentFragmentManager, "Dialog")
    }

    private fun goToDetails(favorites: Favorites) {
        findNavController().navigate(R.id.action_favoriteFragment_to_detailFragment,
            Bundle().apply {
                putParcelable("FAVORITE", favorites)
            })
    }

    private fun setRecycler(characterList: List<Results>) {
        setAdapter(characterList)
        binding.rvFavorite.apply {
            adapter = favoriteAdapter
            setHasFixedSize(true)
        }
    }
}