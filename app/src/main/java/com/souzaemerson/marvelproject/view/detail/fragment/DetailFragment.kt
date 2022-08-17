package com.souzaemerson.marvelproject.view.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.souzaemerson.marvelproject.util.setVisibilityAs
import com.souzaemerson.marvelproject.util.ts
import com.souzaemerson.marvelproject.view.detail.adapter.CarouselAdapter
import com.souzaemerson.marvelproject.view.detail.adapter.ComicsAndSeriesFields
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
            setCharacterScreen()
            setFavoriteCharacter()
        }

        observeVMEvents()
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
                            binding.fabDetails.setIconResource(R.drawable.ic_favorite)
                            binding.fabDetails.setText(R.string.FAVORITADO_FAB)
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

    private fun CharacterDetailsBinding.setCharacterScreen() {
        setImage(imgDetail)
        detailsTitle.text = favorite.name
        detailsDescription.text = favorite.description
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
                fabDetails.setText(R.string.FAVORITAR_FAB)
                fabDetails.setIconResource(R.drawable.ic_baseline_favorite_border_24)
                false
            } else {
                val copyFavorite = favorite.copy(email = user.email)
                viewModel.insertFavorite(copyFavorite)
                fabDetails.setText(R.string.FAVORITADO_FAB)
                fabDetails.setIconResource(R.drawable.ic_favorite)
                true
            }
        }
    }

    private fun getCategory(id: Long) {
        val ts = ts()
        viewModel.getCategory(apikey(), hash(ts), ts.toLong(), id)
    }

    private fun getSeries(id: Long) {
        val ts = ts()
        viewModel.getSeries(apikey(), hash(ts), ts.toLong(), id)
    }

    private fun setAdapter(list: List<Result>) {
        carouselAdapter = CarouselAdapter(list){
            binding.run {
                ComicsAndSeriesFields(it).run {
                    setBaseFieldsOfScreen(it)

                    setVisibilityAs(fabDetails, View.INVISIBLE)
                    setVisibilityAs(fabBackToCharacter, View.VISIBLE)

                    backToCharacterButton()

                    val textPrices = "Price:\n$ ${it.prices?.first()?.price}"
                    setPricesFields(comicsPrice, textPrices)

                    val textPages = "Pages:\n${it.pageCount}"
                    setPageFields(comicsPages, textPages)

                    val textOnSaleDate = "Release Date:\n${it.dates?.first()?.date?.substring(0,10)
                        ?.replace("-", "/")}"
                    setOnSaleDateFields(comicsOnSaleDate, textOnSaleDate)
                }
            }
        }
    }

    private fun CharacterDetailsBinding.setBaseFieldsOfScreen(
        item: Result
    ) {
        Glide.with(this@DetailFragment)
            .load("${item.thumbnail.path}.${item.thumbnail.extension}")
            .into(imgDetail)
        detailsTitle.text = item.title
        detailsDescription.text = item.description
    }

    private fun CharacterDetailsBinding.backToCharacterButton() {
        fabBackToCharacter.setOnClickListener {
            setCharacterScreen()
            fabBackToCharacter.visibility = View.GONE
            fabDetails.visibility = View.VISIBLE
            comicsPrice.visibility = View.INVISIBLE
            comicsPages.visibility = View.INVISIBLE
            comicsOnSaleDate.visibility = View.INVISIBLE
        }
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

    private fun setRecyclerSeries(list: List<Result>) {
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