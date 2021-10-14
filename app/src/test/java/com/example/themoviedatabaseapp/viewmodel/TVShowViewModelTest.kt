package com.example.themoviedatabaseapp.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.themoviedatabaseapp.di.TVShowApp
import com.example.themoviedatabaseapp.model.current.CurResult
import com.example.themoviedatabaseapp.model.current.CurrentTVShowList
import com.example.themoviedatabaseapp.model.today.TdResult
import com.example.themoviedatabaseapp.model.today.TodayTVShowList
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails
import com.example.themoviedatabaseapp.repository.TVRepo
import com.example.themoviedatabaseapp.util.TestCoroutineRule
import com.example.themoviedatabaseapp.utils.TVViewState
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.UnknownHostException
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class) //Tells Mockito to create the mocks based on the @Mock annotation
class TVShowViewModelTest {

    // Needed to test codes with LiveData or class that use Architecture Components.
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    //declare all properties here
    private val showTdTVListObserver: Observer<List<TdResult>> = mock() //create mock objects
    private val showTVDetailsObserver: Observer<TVShowDetails> = mock()
    private val showCurTVListObserver: Observer<List<CurResult>> = mock()
    private val errorMessageObserver: Observer<String> = mock()
    private val loadingStateObserver: Observer<TVShowViewModel.LoadingState> = mock()
    private lateinit var curTvShow: CurrentTVShowList
    private lateinit var tdTvShow: TodayTVShowList
    private var tdResultList = mutableListOf<TdResult>()
    private var curResultList = mutableListOf<CurResult>()
    private lateinit var tvDetails: TVShowDetails
    private val viewStateObserver: Observer<TVViewState.ViewState> = mock()
    private lateinit var tvShowViewModel: TVShowViewModel
    private lateinit var context: Context


    @Mock //creates mock objects
    private lateinit var tvRepo: TVRepo

    @Mock
    private lateinit var mFirebaseDatabaseInstances: FirebaseDatabase

    @Mock
    private lateinit var mFirebaseDatabase: DatabaseReference

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this) //triggers the initialization of the @Mock annotated fields
        tvShowViewModel = TVShowViewModel(tvRepo)
        context  = TVShowApp.getContext()
        FirebaseApp.initializeApp(context)
        mFirebaseDatabase = mFirebaseDatabaseInstances.getReference("tvShowDetails")
        tvShowViewModel.loadingState.observeForever(loadingStateObserver)
        tvShowViewModel.curTVLiveData().observeForever(showCurTVListObserver)
        tvShowViewModel.todayTVLiveData().observeForever(showTdTVListObserver)
        tvShowViewModel.tvDetails().observeForever(showTVDetailsObserver)
        tvShowViewModel.errorMessage().observeForever(errorMessageObserver)
        tdResultList.add(TdResult("any", 11, "any", "any", 0.2))
        curResultList.add(CurResult("any", 12, "any", "any", 0.1))
        curTvShow = CurrentTVShowList(curResultList)
        tdTvShow = TodayTVShowList(tdResultList)
        tvDetails = TVShowDetails()
        tvShowViewModel.viewState.observeForever(viewStateObserver)
    }

    @Test
    fun fetchCurTv_ReturnData_WithSuccess(): Unit =
        testCoroutineRule.runBlockingTest {
            //Given the setup below
            //run the mocked the dependency to return success - a list.
           // doReturn(curTvShow).`when`(tvRepo).getTVCurrent()
            `when`(tvRepo.getTVCurrent()).thenReturn(curTvShow)

            //When you run the system/class under test
            tvShowViewModel.tvCurrentFromViewModel() // Then, we fetch...

            //Then verify the following behaviour
            verify(tvRepo, atLeast(1)).getTVCurrent()
            verify(showCurTVListObserver, atLeast(1))
                .onChanged(curResultList)
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.SUCCESS)
            verify(errorMessageObserver, atLeast(0))
                .onChanged("any")
        }


    @Test
    fun fetchTdTv_ReturnData_WithSuccess(): Unit =
        testCoroutineRule.runBlockingTest {
            doReturn(tdTvShow).`when`(tvRepo).getTVToday()
            tvShowViewModel.tvTodayFromViewModel()

            verify(tvRepo, atLeast(1)).getTVToday()
            verify(showTdTVListObserver, atLeast(1))
                .onChanged(tdResultList)
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.SUCCESS)
            verify(errorMessageObserver, atLeast(0))
                .onChanged("any")
        }


    @Test
    fun fetchCurTv_NoReturnData_EmptyList(): Unit =
        testCoroutineRule.runBlockingTest {
            val emptyCurTvShow = CurrentTVShowList(emptyList())
            doReturn(emptyCurTvShow).`when`(tvRepo).getTVCurrent()
            tvShowViewModel.tvCurrentFromViewModel()

            verify(tvRepo, atLeast(1)).getTVCurrent()
            verify(showCurTVListObserver, atLeast(0))
                .onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1))
                .onChanged("No Data Found")
        }


    @Test
    fun fetchTdTv_NoReturnData_EmptyList(): Unit =
        testCoroutineRule.runBlockingTest {
            val emptyTdTvShow = TodayTVShowList(emptyList())
            doReturn(emptyTdTvShow).`when`(tvRepo).getTVToday()
            tvShowViewModel.tvTodayFromViewModel()

            verify(tvRepo, atLeast(1)).getTVToday()
            verify(showTdTVListObserver, atLeast(0))
                .onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1))
                .onChanged("No Data Found")
        }


    @Test
    fun fetchCurTv_NoReturnData_NoNetwork(): Unit =
        testCoroutineRule.runBlockingTest {
            doThrow(UnknownHostException("NO Network!"))
                .`when`(tvRepo).getTVCurrent()
            tvShowViewModel.tvCurrentFromViewModel()

            verify(tvRepo, atLeast(1)).getTVCurrent()
            verify(showCurTVListObserver, atLeast(0))
                .onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1))
                .onChanged("No Network!")
        }


    @Test
    fun fetchTdTv_NoReturnData_NoNetwork(): Unit =
        testCoroutineRule.runBlockingTest {
            doThrow(UnknownHostException("No Network!"))
                .`when`(tvRepo).getTVToday()
            tvShowViewModel.tvTodayFromViewModel()

            verify(tvRepo, atLeast(1)).getTVToday()
            verify(showTdTVListObserver, atLeast(0))
                .onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1))
                .onChanged("No Network!")
        }


    @Test
    fun fetchTdTV_NoReturnData_WithError(): Unit =
        testCoroutineRule.runBlockingTest {
            doThrow(RuntimeException("Something Wrong, no blank or empty field"))
                .`when`(tvRepo).getTVToday()
            tvShowViewModel.tvTodayFromViewModel()

            verify(tvRepo, atLeast(1)).getTVToday()
            verify(showTdTVListObserver, atLeast(0))
                .onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1))
                .onChanged("Something Wrong, no blank or empty field")
        }


    @Test
    fun fetchCurTV_NoReturnData_WithError(): Unit =
        testCoroutineRule.runBlockingTest {
            doThrow(RuntimeException("Something Wrong, no blank or empty field"))
                .`when`(tvRepo).getTVCurrent()
            tvShowViewModel.tvCurrentFromViewModel()

            verify(tvRepo, atLeast(1)).getTVCurrent()
            verify(showCurTVListObserver, atLeast(0))
                .onChanged(emptyList())
            verify(loadingStateObserver, atLeast(1))
                .onChanged(TVShowViewModel.LoadingState.ERROR)
            verify(errorMessageObserver, atLeast(1))
                .onChanged("Something Wrong, no blank or empty field")
        }


    @Test
    fun fetchTvDetails_Return_WithSuccess_From_Network(): Unit =
        testCoroutineRule.runBlockingTest {
            val tvID = 1211
            `when`(tvRepo.checkIfData(tvID)).thenReturn(0)
            tvShowViewModel.getDetails(tvID)

            verify(tvRepo, atLeast(1)).getTVDetail(tvID)
            verify(tvRepo, atLeast(0)).getTVFromDB(tvID)
            verify(viewStateObserver, atLeast(1))
                .onChanged(TVViewState.Success(tvDetails))
            verify(viewStateObserver, atLeast(0))
                .onChanged(TVViewState.Error(any()))
        }

    @Test
    fun fetchTvDetails_Return_WithSuccess_From_Db(): Unit =
        testCoroutineRule.runBlockingTest {
            val tvID = 1211
            `when`(tvRepo.checkIfData(tvID)).thenReturn(1)
            tvShowViewModel.getDetails(tvID)

            verify(tvRepo, atLeast(0)).getTVDetail(tvID)
            verify(tvRepo, atLeast(1)).getTVFromDB(tvID)
            verify(viewStateObserver, atLeast(1))
                .onChanged(TVViewState.Success(tvDetails))
            verify(viewStateObserver, atLeast(0))
                .onChanged(TVViewState.Error(any()))
        }
}