package com.souzaemerson.marvelproject.view.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.core.Status
import com.souzaemerson.marvelproject.data.db.AppDatabase
import com.souzaemerson.marvelproject.data.db.dao.CharacterDAO
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepository
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.souzaemerson.marvelproject.data.model.Favorites
import com.souzaemerson.marvelproject.data.model.User
import com.souzaemerson.marvelproject.data.model.comic.Result
import com.souzaemerson.marvelproject.data.network.ApiService
import com.souzaemerson.marvelproject.data.repository.category.CategoryRepository
import com.souzaemerson.marvelproject.data.repository.category.CategoryRepositoryImpl
import com.souzaemerson.marvelproject.databinding.CharacterDetailsBinding
import com.souzaemerson.marvelproject.util.apikey
import com.souzaemerson.marvelproject.util.hash
import com.souzaemerson.marvelproject.util.toast
import com.souzaemerson.marvelproject.util.ts
import com.souzaemerson.marvelproject.view.detail.adapter.CarouselAdapter
import com.souzaemerson.marvelproject.view.detail.decoration.BoundsOffsetDecoration
import com.souzaemerson.marvelproject.view.detail.decoration.LinearHorizontalSpacingDecoration
import com.souzaemerson.marvelproject.view.detail.decoration.ProminentLayoutManager
import com.souzaemerson.marvelproject.view.detail.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var repository: DatabaseRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var snapHelper: SnapHelper
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var binding: CharacterDetailsBinding
    private lateinit var result: Result
    private lateinit var favorite: Favorites
    private lateinit var user: User
    private var checkCharacter: Boolean = false
    private val dao: CharacterDAO by lazy {
        AppDatabase.getDb(requireContext()).characterDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favorite = arguments?.getParcelable<Favorites>("FAVORITE") as Favorites
        repository = DatabaseRepositoryImpl(dao)
        categoryRepository = CategoryRepositoryImpl(ApiService.service)
        viewModel = DetailViewModel.DetailViewModelProviderFactory(
            repository, Dispatchers.IO, categoryRepository
        ).create(DetailViewModel::class.java)

        getUserByIntent()

        viewModel.verifySavedCharacter(favorite.id, user.email)
        getCategory(id = favorite.id)
        getSeries(id = favorite.id)

        binding.run {
            setImage(imgDetail)

            detailsTitle.text = favorite.name
            detailsDescription.text = favorite.description

            setFavoriteCharacter()
        }

        observeVMEvents()
    }

    private fun getUserByIntent() {
        activity?.let {
            user = it.intent.getParcelableExtra<User>("USER") as User
        }
    }

    private fun CharacterDetailsBinding.setFavoriteCharacter() {
        fabDetails.setOnClickListener {

            checkCharacter = if (checkCharacter) {
                val newFavorite = favorite.copy(email = user.email)
                viewModel.deleteCharacter(newFavorite)
                fabDetails.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                false
            } else {
                val copyFavorite = favorite.copy(email = user.email)
                viewModel.insertFavorite(copyFavorite)
                fabDetails.setImageResource(R.drawable.ic_favorite)
                true
            }
        }
    }

    private fun getCategory(id: Long){
        val ts = ts()
        viewModel.getCategory(apikey(), hash(ts), ts.toLong(), id)
    }

    private fun getSeries(id: Long){
        val ts = ts()
        viewModel.getSeries(apikey(), hash(ts), ts.toLong(), id)
    }

    private fun observeVMEvents() {
        viewModel.response.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        Timber.tag("Success").i(response.toString())
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
        viewModel.comicsResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { category ->
                        setRecycler(category.data.results)
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
        viewModel.seriesResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { category ->
                        setRecyclerSeries(category.data.results)
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
        viewModel.verifyCharacter.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { exist ->
                        if (exist) {
                            binding.fabDetails.setImageResource(R.drawable.ic_favorite)
                        }
                        checkCharacter = exist
                    }
                }
                Status.ERROR -> {
                    Timber.tag("Error").i(it.error)
                }
                Status.LOADING -> {}
            }
        }
    }

    private fun setAdapter(list: List<Result>) {
        carouselAdapter = CarouselAdapter(list)
    }

    private fun setRecycler(list: List<Result>) {
        setAdapter(list)
        snapHelper = PagerSnapHelper()

        binding.run {
            recyclerCategory.apply {
                adapter = carouselAdapter
                layoutManager = ProminentLayoutManager(context)
                setItemViewCacheSize(4)
                val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
                addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
                addItemDecoration(BoundsOffsetDecoration())
            }
        }

        snapHelper.attachToRecyclerView(binding.recyclerCategory)
    }

    private fun setRecyclerSeries(list: List<Result>){
        setAdapter(list)
        snapHelper = PagerSnapHelper()

        binding.run {
            recyclerCategorySeries.apply {
                adapter = carouselAdapter
                layoutManager = ProminentLayoutManager(context)
                setItemViewCacheSize(4)
                val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
                addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
                addItemDecoration(BoundsOffsetDecoration())
            }
        }

        snapHelper.attachToRecyclerView(binding.recyclerCategorySeries)
    }

    private fun setImage(image: AppCompatImageView) {
        Glide.with(this@DetailFragment)
            .load("${favorite.thumbnail.path}.${favorite.thumbnail.extension}")
            .centerCrop()
            .into(image)
    }
}