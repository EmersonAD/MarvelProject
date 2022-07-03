package com.souzaemerson.marvelproject.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.core.Status
import com.souzaemerson.marvelproject.data.db.AppDatabase
import com.souzaemerson.marvelproject.data.db.CharacterDAO
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepository
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.databinding.CharacterDetailsBinding
import com.souzaemerson.marvelproject.view.fragment.detail.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailFragment : Fragment() {
    lateinit var viewModel: DetailViewModel
    lateinit var repository: DatabaseRepository
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }
    private lateinit var binding: CharacterDetailsBinding
    private lateinit var character: Results

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        character = arguments?.getSerializable("CHARACTER") as Results

        repository = DatabaseRepositoryImpl(dao)
        viewModel = DetailViewModel.DetailViewModelProviderFactory(repository, Dispatchers.IO)
            .create(DetailViewModel::class.java)


        binding.run {

            getGlide(imgPoster)

            getGlide(imgDetail)

            txtTitleDetails.text = character.name
            txtDescriptionDetails.text = character.description

            fabDetails.setOnClickListener {
                viewModel.insertCharacters(character)
                fabDetails.setImageResource(R.drawable.ic_favorite)
            }
        }

        setColorHeart(character.id)
        observeVMEvents()
    }

    private fun setColorHeart(characterId: Long) {
        lifecycleScope.launch {
            dao.getFavoriteCharacter(characterId)?.let {
                binding.fabDetails.setImageResource(R.drawable.ic_favorite)
            }
        }
    }

    private fun observeVMEvents(){
        viewModel.response.observe(viewLifecycleOwner){

            when(it.status){
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        Timber.tag("Success").i(response.toString())
                    }
                }
                Status.ERROR ->{
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING ->{}
            }
        }
    }

    private fun getGlide(image: AppCompatImageView) =
        Glide.with(this@DetailFragment)
            .load("${character.thumbnail.path}.${character.thumbnail.extension}")
            .centerCrop()
            .into(image)
}
