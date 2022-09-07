package com.souzaemerson.marvelproject.view.home.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.core.BaseFragment
import com.souzaemerson.marvelproject.core.Status
import com.souzaemerson.marvelproject.core.hasInternet
import com.souzaemerson.marvelproject.core.preferences.PreferencesUtil
import com.souzaemerson.marvelproject.data.model.CharacterResponse
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.data.model.User
import com.souzaemerson.marvelproject.data.repository.character.CharacterRepository
import com.souzaemerson.marvelproject.databinding.FragmentHomeBinding
import com.souzaemerson.marvelproject.util.*
import com.souzaemerson.marvelproject.view.adapter.CharacterAdapter
import com.souzaemerson.marvelproject.view.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val viewModel by viewModels<HomeViewModel>()
    lateinit var repository: CharacterRepository
    lateinit var binding: FragmentHomeBinding
    private lateinit var user: User
    private lateinit var characterAdapter: CharacterAdapter
    private var offsetCharacters: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupToolbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("CONNECTION").i(hasInternet(context).toString())

        activity?.let {
            user = it.intent?.getParcelableExtra<User>("USER") as User
        }
        toast(user.email)

        paginationSetup()
        checkConnection()
        observeVMEvents()
    }

    private fun search(menu: Menu) {
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Pesquisar"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                when (newText) {
                    "" -> {
                        getCharacters(offset = offsetCharacters)
                    }
                    else -> {
                        searchCharacter(newText.toString())
                    }
                }
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
        search(menu)
    }

    private fun paginationSetup() {
        binding.fabItemNext.setOnClickListener {
            if (offsetCharacters >= 0) {
                offsetCharacters += 50
                getCharacters(offset = offsetCharacters)
            }
        }
        binding.fabItemPrevious.setOnClickListener {
            if (offsetCharacters >= 50) {
                offsetCharacters -= 50
                getCharacters(offset = offsetCharacters)
            }
        }
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.includeToolbar.toolbarLayout)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun checkConnection() {
        if (hasInternet(context)) {
            getCharacters()
        } else {
            ConfirmDialog(
                getString(R.string.conection_error),
                getString(R.string.verifiry_your_internet),
                getString(R.string.try_again),
                getString(R.string.negative_no)
            ).apply {
                setListener {
                    checkConnection()
                }
            }.show(parentFragmentManager, "Connection")
        }
    }

    private fun getCharacters(limit: Int = 50, offset: Int = 0) {
        val ts = ts()
        viewModel.getCharacters(apikey(), hash(ts), ts.toLong(), limit, offset)
    }

    private fun searchCharacter(nameStart: String) {
        val ts = ts()
        viewModel.searchCharacter(nameStart, apikey(), hash(ts), ts.toLong())
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        Timber.tag("Sucesso").i(response.toString())
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
        viewModel.search.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { searchResponse ->
                        Timber.tag("Sucesso").i(searchResponse.toString())
                        setRecyclerView(searchResponse.data.results)
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
        viewModel.responseInCache.observe(viewLifecycleOwner) { characterList ->
            setRecyclerView(characterList)
        }
    }

    private fun setAdapter(characterList: List<Results>) {
        characterAdapter = CharacterAdapter(characterList, {
            Timber.tag("Click").i(it.name)
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment,
                Bundle().apply {
                    putParcelable("FAVORITE", it)
                })
        })
    }

    private fun setRecyclerView(characterList: List<Results>) {
        setAdapter(characterList = characterList)
        binding.rvTest.apply {
            setHasFixedSize(true)
            adapter = characterAdapter
        }
    }
}