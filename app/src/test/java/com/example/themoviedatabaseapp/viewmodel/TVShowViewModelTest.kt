package com.example.themoviedatabaseapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.themoviedatabaseapp.model.current.CurResult
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TdResult
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.repository.TVRepo
import com.example.themoviedatabaseapp.util.TestCoroutineRule
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class) //Tells Mockito to create the mocks based on the @Mock annotation
class TVShowViewModelTest {

    // Needed to test codes with LiveData or class that use Architecture Components.
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    //declare all properties here
    private lateinit var tvShowViewModel: TVShowViewModel
    private val showTdTVListObserver: Observer<List<TdResult>> = mock() //create mock objects
    private val showTVDetailsObserver: Observer<TVShowDetails> = mock()
    private val showCurTVListObserver: Observer<List<CurResult>> = mock()
    private val errorMessageObserver: Observer<String> = mock()
    private val loadingStateObserver: Observer<TVShowViewModel.LoadingState> = mock()
    private lateinit var curTvshow: CurrentTVShowList
    private lateinit var tdTvshow: TodayTVShowList
    private var tdResultList = mutableListOf<TdResult>()
    private var curResultList = mutableListOf<CurResult>()
    private val tvRepo: TVRepo = mock()

    /*@Mock //creates mock objects
    private lateinit var tvRepo: TVRepo
*/
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) //triggers the initialization of the @Mock annotated fields
        tvShowViewModel = TVShowViewModel(tvRepo)
        tvShowViewModel.loadingState.observeForever(loadingStateObserver)
        tvShowViewModel.curTVLiveData().observeForever(showCurTVListObserver)
        tvShowViewModel.todayTVLiveData().observeForever(showTdTVListObserver)
        tvShowViewModel.tvDetails().observeForever(showTVDetailsObserver)
        tvShowViewModel.errorMessage().observeForever(errorMessageObserver)
        tdResultList.add(TdResult("any", 11, "any", "any", 0.2))
        curResultList.add(CurResult("any", 12, "any", "any", 0.1))
        curTvshow = CurrentTVShowList(curResultList)
        tdTvshow = TodayTVShowList(tdResultList)
    }

    @Test
    fun fetchCurTv_ReturnData_WithSuccess() {
        testCoroutineRule.runBlockingTest {
            //run the mocked the dependency to return the success with a list.
            curTvshow = CurrentTVShowList(curResultList)
            tvShowViewModel = TVShowViewModel(tvRepo)
            doReturn(curTvshow).`when`(tvRepo).getTVCurrent()

            tvShowViewModel.tvCurrentFromViewModel() // Then, we fetch...

            //and verify.
            verify(tvRepo, atLeast(1)).getTVCurrent()
            verify(showCurTVListObserver, atLeast(1)).onChanged(curResultList)
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.SUCCESS)
            verify(errorMessageObserver, atLeast(0)).onChanged("any")
        }
    }

    @Test
    suspend fun fetchTdTv_ReturnData_WithSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(tdTvshow).`when`(tvRepo).getTVToday()
            tvShowViewModel.tvTodayFromViewModel()

            verify(tvRepo, atLeast(1)).getTVToday()
            verify(showTdTVListObserver, atLeast(1)).onChanged(tdResultList)
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.SUCCESS)
            verify(errorMessageObserver, atLeast(0)).onChanged("any")
        }
    }



    @Test
    suspend fun fetchCurTv_NoReturnData_EmptyList() {
        testCoroutineRule.runBlockingTest {
            val emptyCurTvShow = CurrentTVShowList(emptyList())
            doReturn(emptyCurTvShow).`when`(tvRepo).getTVCurrent()
            tvShowViewModel.tvCurrentFromViewModel()

            verify(tvRepo, atLeast(1)).getTVCurrent()
            verify(showCurTVListObserver, atLeast(0)).onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1)).onChanged("No Data Found")
        }
    }

    @Test
    suspend fun fetchTdTv_NoReturnData_EmptyList() {
        testCoroutineRule.runBlockingTest {
            val emptyTdTvShow = TodayTVShowList(emptyList())
            doReturn(emptyTdTvShow).`when`(tvRepo).getTVToday()
            tvShowViewModel.tvTodayFromViewModel()

            verify(tvRepo, atLeast(1)).getTVToday()
            verify(showTdTVListObserver, atLeast(0)).onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1)).onChanged("No Data Found")
        }
    }

    @Test
    suspend fun fetchCurTv_NoReturnData_NoNetwork() {
        testCoroutineRule.runBlockingTest {
            doThrow(UnknownHostException("NO Network!")).`when`(tvRepo).getTVCurrent()
            tvShowViewModel.tvCurrentFromViewModel()

            verify(tvRepo, atLeast(1)).getTVCurrent()
            verify(showCurTVListObserver, atLeast(0)).onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1)).onChanged("No Network!")
        }
    }

    @Test
    suspend fun fetchTdTv_NoReturnData_NoNetwork() {
        testCoroutineRule.runBlockingTest {
            doThrow(UnknownHostException("No Network!")).`when`(tvRepo).getTVToday()
            tvShowViewModel.tvTodayFromViewModel()

            verify(tvRepo, atLeast(1)).getTVToday()
            verify(showTdTVListObserver, atLeast(0)).onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1)).onChanged("No Network!")
        }
    }

    @Test
    suspend fun fetchTdTV_NoReturnData_WithError() {
        testCoroutineRule.runBlockingTest {
            doThrow(RuntimeException("Something Wrong, no blank or empty field"))
                .`when`(tvRepo).getTVToday()
            tvShowViewModel.tvTodayFromViewModel()

            verify(tvRepo, atLeast(1)).getTVToday()
            verify(showTdTVListObserver, atLeast(0)).onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1))
                .onChanged("Something Wrong, no blank or empty field")
        }
    }

    @Test
    suspend fun fetchCurTV_NoReturnData_WithError() {
        testCoroutineRule.runBlockingTest {
            doThrow(RuntimeException("Something Wrong, no blank or empty field"))
                .`when`(tvRepo).getTVCurrent()
            tvShowViewModel.tvCurrentFromViewModel()

            verify(tvRepo, atLeast(1)).getTVCurrent()
            verify(showCurTVListObserver, atLeast(0)).onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1))
                .onChanged("Something Wrong, no blank or empty field")
        }
    }
}