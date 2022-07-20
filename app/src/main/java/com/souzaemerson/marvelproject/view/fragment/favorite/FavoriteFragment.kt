package com.souzaemerson.marvelproject.view.fragment.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.core.Status
import com.souzaemerson.marvelproject.data.db.AppDatabase
import com.souzaemerson.marvelproject.data.db.CharacterDAO
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepository
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.databinding.FragmentFavoriteBinding
import com.souzaemerson.marvelproject.util.ConfirmDialog
import com.souzaemerson.marvelproject.view.adapter.CharacterAdapter
import com.souzaemerson.marvelproject.view.fragment.favorite.viewmodel.FavoriteViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class FavoriteFragment : Fragment() {
    lateinit var viewModel: FavoriteViewModel
    lateinit var repository: DatabaseRepository
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = DatabaseRepositoryImpl(dao)
        viewModel = FavoriteViewModel(repository, Dispatchers.IO)

        observeVMEvents()
    }

    private fun observeVMEvents() {
        viewModel.getCharacters().observe(viewLifecycleOwner) { results ->
            when {
                results.isNotEmpty() -> {
                    Timber.tag("LISTARESULTADO").i(results.toString())
                    setRecycler(results)
                }
                else -> {
                    setRecycler(results)
                }
            }
        }
        viewModel.delete.observe(viewLifecycleOwner) { state ->
            when (state.status) {
                Status.SUCCESS -> {
                    state.data?.let {
                        Toast.makeText(requireContext(), "Personagem deletado", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }

    private fun setAdapter(characterList: List<Results>) {
        favoriteAdapter = CharacterAdapter(characterList, ::goToDetails, ::deleteCharacter)
    }

    private fun deleteCharacter(character: Results) {
        ConfirmDialog(
            getString(R.string.confirmation),
            getString(R.string.message_connection)
        ).apply {
            setListener {
                viewModel.deleteCharacter(character)
            }
        }.show(parentFragmentManager, "Dialog")
    }

    private fun goToDetails(character: Results) {
        findNavController().navigate(R.id.action_favoriteFragment_to_detailFragment,
            Bundle().apply {
                putSerializable("CHARACTER", character)
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