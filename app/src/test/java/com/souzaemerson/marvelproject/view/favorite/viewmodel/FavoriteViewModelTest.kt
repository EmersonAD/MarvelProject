package com.souzaemerson.marvelproject.view.favorite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.souzaemerson.marvelproject.core.State
import com.souzaemerson.marvelproject.core.Status
import com.souzaemerson.marvelproject.data.db.repository.DatabaseRepositoryImpl
import com.souzaemerson.marvelproject.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

    @get:Rule
    val instantTestExecutorRule = InstantTaskExecutorRule()

    private val testCoroutinesDispatcher = TestCoroutineDispatcher()
    private val repositoryMock = mock(DatabaseRepositoryImpl::class.java)
    private val dispatcherMock = mock(Dispatchers::class.java)
    private lateinit var viewModel: FavoriteViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = FavoriteViewModel(repositoryMock, dispatcherMock.IO)
    }

    @After
    fun tearDown() {
        testCoroutinesDispatcher.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    @Test
    fun `test do test`() = testCoroutinesDispatcher.runBlockingTest {
        doReturn(true).`when`(repositoryMock).deleteCharacter(mockFavorite)

        viewModel.deleteCharacter(mockFavorite)

        Truth.assertThat(viewModel.delete.value).isEqualTo(State(Status.SUCCESS, loading = false, data = true, error = null))
    }

    private val mockFavorite = Favorites(
        1,
        "",
        "",
        "",
        Thumbnail("", ""),
        "",
        Comics(1, "", emptyList(), 1),
        Series(1, "", emptyList(), 1),
        Stories(1, "", emptyList(), 1),
        Events(1, "", emptyList(), 1),
        emptyList(),
        ""
    )
}