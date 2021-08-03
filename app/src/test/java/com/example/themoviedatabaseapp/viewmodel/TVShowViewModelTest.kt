package com.example.themoviedatabaseapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.themoviedatabaseapp.model.current.CurResult
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TdResult
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.repository.TVRepo
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner::class)
class TVShowViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    //declare all properties here
    private lateinit var tvShowViewModel: TVShowViewModel
    private val showTdTVListObserver: Observer<List<TdResult>> = mock()
    private val showTVDetailsObserver: Observer<TVShowDetails> = mock()
    private val showCurTVListObserver: Observer<List<CurResult>> = mock()
    private val errorMessageObserver: Observer<String> = mock()
    private val loadingStateObserver: Observer<TVShowViewModel.LoadingState> = mock()
    private lateinit var curTvshow: CurrentTVShowList
    private lateinit var tdTvshow: TodayTVShowList
    private var tdResultList = mutableListOf<TdResult>()
    private var curResultList = mutableListOf<CurResult>()

    @Mock
    private lateinit var tvRepo: TVRepo

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        tvShowViewModel = TVShowViewModel(tvRepo)
        tvShowViewModel.loadingState.observeForever(loadingStateObserver)
        tvShowViewModel.curTVLiveData().observeForever(showCurTVListObserver)
        tvShowViewModel.todayTVLiveData().observeForever(showTdTVListObserver)
        tvShowViewModel.tvDetails().observeForever(showTVDetailsObserver)
        tvShowViewModel.errorMessage().observeForever(errorMessageObserver)
        tdResultList.add(TdResult("any",11, "any","any",0.2))
        curResultList.add(CurResult("any",12, "any","any",0.1))
        curTvshow = CurrentTVShowList(curResultList)
        tdTvshow = TodayTVShowList(tdResultList)
    }

    @Test
    fun fetchCurTv_ReturnData_WithSuccess(){
        `when`(tvRepo.getTVCurrent()).thenReturn(Single.just(curTvshow))
        tvShowViewModel.tvCurrentFromViewModel()

        verify(tvRepo, atLeast(1)).getTVCurrent()
        verify(showCurTVListObserver, atLeast(1)).onChanged(curResultList)
        verify(loadingStateObserver, atLeast(1))
            .onChanged(TVShowViewModel.LoadingState.SUCCESS)
        verify(errorMessageObserver, atLeast(0)).onChanged("any")
    }

    @Test
    fun fetchTdTv_ReturnData_WithSuccess(){
        `when`(tvRepo.getTVToday()) .thenReturn(Single.just(tdTvshow))
        tvShowViewModel.tvTodayFromViewModel()

        verify(tvRepo, atLeast(1)).getTVToday()
        verify(showTdTVListObserver, atLeast(1)).onChanged(tdResultList)
        verify(loadingStateObserver, atLeast(1))
            .onChanged(TVShowViewModel.LoadingState.SUCCESS)
        verify(errorMessageObserver, atLeast(0)).onChanged("any")
    }

    @Test
    fun fetchCurTv_NoReturnData_EmptyList(){
        val emptyCurTvShow = CurrentTVShowList(emptyList())
        `when`(tvRepo.getTVCurrent()).thenReturn(Single.just(emptyCurTvShow))
        tvShowViewModel.tvCurrentFromViewModel()

        verify(tvRepo, atLeast(1)).getTVCurrent()
        verify(showCurTVListObserver, atLeast(0)).onChanged(emptyList())
        verify(loadingStateObserver, atLeast(1))
            .onChanged(TVShowViewModel.LoadingState.ERROR)
        verify(errorMessageObserver, atLeast(1)).onChanged("No Data Found")
    }

    @Test
    fun fetchTdTv_NoReturnData_EmptyList(){
        val emptyTdTvShow = TodayTVShowList(emptyList())
        `when`(tvRepo.getTVToday()) .thenReturn(Single.just(emptyTdTvShow))
        tvShowViewModel.tvTodayFromViewModel()

        verify(tvRepo, atLeast(1)).getTVToday()
        verify(showTdTVListObserver, atLeast(0)).onChanged(emptyList())
        verify(loadingStateObserver, atLeast(1))
            .onChanged(TVShowViewModel.LoadingState.ERROR)
        verify(errorMessageObserver, atLeast(1)).onChanged("No Data Found")
    }

    @Test
    fun fetchCurTv_NoReturnData_NoNetwork(){
        `when`(tvRepo.getTVCurrent()).thenReturn(Single.error(
            UnknownHostException("No Network!")))
        tvShowViewModel.tvCurrentFromViewModel()

        verify(tvRepo, atLeast(1)).getTVCurrent()
        verify(showCurTVListObserver, atLeast(0)).onChanged(emptyList())
        verify(loadingStateObserver, atLeast(1))
            .onChanged(TVShowViewModel.LoadingState.ERROR)
        verify(errorMessageObserver, atLeast(1)).onChanged("No Network!")
    }

    @Test
    fun fetchTdTv_NoReturnData_NoNetwork(){
        `when`(tvRepo.getTVToday()).thenReturn(Single.error(
            UnknownHostException("No Network!")))
        tvShowViewModel.tvTodayFromViewModel()

        verify(tvRepo, atLeast(1)).getTVToday()
        verify(showTdTVListObserver, atLeast(0)).onChanged(emptyList())
        verify(loadingStateObserver, atLeast(1))
            .onChanged(TVShowViewModel.LoadingState.ERROR)
        verify(errorMessageObserver, atLeast(1)).onChanged("No Network!")
    }

    @Test
    fun fetchTdTV_NoReturnData_WithError(){
        `when`(tvRepo.getTVToday()).thenReturn(Single.error(
            RuntimeException("Something Wrong, no blank or empty field")))
        tvShowViewModel.tvTodayFromViewModel()

        verify(tvRepo, atLeast(1)).getTVToday()
        verify(showTdTVListObserver, atLeast(0)).onChanged(emptyList())
        verify(loadingStateObserver, atLeast(1))
            .onChanged(TVShowViewModel.LoadingState.ERROR)
        verify(errorMessageObserver, atLeast(1))
            .onChanged("Something Wrong, no blank or empty field")
    }

    @Test
    fun fetchCurTV_NoReturnData_WithError(){
        `when`(tvRepo.getTVCurrent()).thenReturn(Single.error(
            RuntimeException("Something Wrong, no blank or empty field")))
        tvShowViewModel.tvCurrentFromViewModel()

        verify(tvRepo, atLeast(1)).getTVCurrent()
        verify(showCurTVListObserver, atLeast(0)).onChanged(emptyList())
        verify(loadingStateObserver, atLeast(1))
            .onChanged(TVShowViewModel.LoadingState.ERROR)
        verify(errorMessageObserver, atLeast(1))
            .onChanged("Something Wrong, no blank or empty field")
    }
}