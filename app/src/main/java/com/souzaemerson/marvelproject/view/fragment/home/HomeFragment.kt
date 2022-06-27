package com.souzaemerson.marvelproject.view.fragment.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.core.BaseFragment
import com.souzaemerson.marvelproject.core.Status
import com.souzaemerson.marvelproject.core.hasInternet
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.data.network.ApiService
import com.souzaemerson.marvelproject.data.repository.CharacterRepository
import com.souzaemerson.marvelproject.data.repository.CharactersRepositoryImpl
import com.souzaemerson.marvelproject.databinding.FragmentHomeBinding
import com.souzaemerson.marvelproject.util.apikey
import com.souzaemerson.marvelproject.util.hash
import com.souzaemerson.marvelproject.util.ts
import com.souzaemerson.marvelproject.view.adapter.CharacterAdapter
import com.souzaemerson.marvelproject.view.fragment.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class HomeFragment : BaseFragment() {
    lateinit var viewModel: HomeViewModel
    lateinit var repository: CharacterRepository

    lateinit var binding: FragmentHomeBinding
    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = CharactersRepositoryImpl(ApiService.service)
        viewModel = HomeViewModel.HomeViewModelProviderFactory(repository, Dispatchers.IO)
            .create(HomeViewModel::class.java)

        checkConnection()
        observeVMEvents()
    }

    override fun checkConnection() {
        if (hasInternet(context)){
            getCharacters()
        }else{
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.connection_error))
                .setMessage(getString(R.string.verify_internet))
                .setPositiveButton(getString(R.string.dialog_confirm)){ _, _ ->}
                .show()
        }
    }

    private fun getCharacters() {
        val ts = ts()
        viewModel.getCharacters(apikey(), hash(ts), ts.toLong())
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe

            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        Timber.tag("Success").i(response.toString())
                        setRecyclerView(response.data.results)
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {
                    Timber.tag("Loading").i(it.loading.toString())
                }
            }
        }
    }

    private fun setAdapter(characterList: List<Results>) {
        characterAdapter = CharacterAdapter(characterList) { character ->
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment,
                Bundle().apply {
                    putSerializable("CHARACTER", character)

                })
        }
    }

    private fun setRecyclerView(characterList: List<Results>) {
        setAdapter(characterList = characterList)
        binding.rvTest.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
    }
}
