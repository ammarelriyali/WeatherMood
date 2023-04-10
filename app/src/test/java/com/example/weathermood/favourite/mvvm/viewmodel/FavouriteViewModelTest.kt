package com.example.weathermood.favourite.mvvm.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weathermood.favourite.ResponseStateFav
import com.example.weathermood.favourite.mvvm.FavouriteViewModel
import com.example.weathermood.model.Current
import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.OneCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class FavouriteViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainDispatcherRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: FavouriteViewModel

    val favouriteLocation = FavouriteLocation(
        longitude = "123.0",
        latitude = "1431.0",
        city = "Blackstone"
    )
    val oneCall = OneCall(
        current = Current(
            clouds = 36.37,
            dt = 7740,
            feels_like = 38.39,
            humidity = 40.41,
            pressure = 42.43,
            temp = 44.45,
            weather = listOf(),
            wind_speed = 46.47
        ), daily = listOf(), hourly = listOf(), lat = 48.49, lon = 50.51
    )

    @Before
    fun start() {
        viewModel = FavouriteViewModel(FakeRepo(mutableListOf(), oneCall))
    }

    @Test
    fun getFavItems_checkListIsNull() = runBlockingTest {
        //gien
        //when get fav from list
        viewModel?.getFavItems()
        delay(1000)
        viewModel.response.first()
        var x = (viewModel.response.first() as ResponseStateFav.Success).data
        //then check if fav equal null
        MatcherAssert.assertThat(x.size, `is`(0))
    }

    @Test
    fun delete_favItem_checkIsListNotEmpty() = runBlockingTest {
        //given item of favItem
        viewModel.insert(favouriteLocation)
        //when get fav from list
        viewModel?.delete(favouriteLocation)
        delay(1000)
        viewModel.response.first()
        viewModel.response.first()
        var x = (viewModel.response.first() as ResponseStateFav.Success).data
        //then check if fav equal null
        MatcherAssert.assertThat(x.size, `is`(0))
    }

    @Test
    fun insert_favItem_checkIsListNotEmpty() = runBlockingTest {
        //given item of favItem
        viewModel.insert(favouriteLocation)
        //when get fav from list
        delay(1000)
        viewModel.response.first()
        var x = (viewModel.response.first() as ResponseStateFav.Success).data
        //then check if fav equal null
        MatcherAssert.assertThat(x.size, `is`(1))
    }

    @Test
    fun getCurrentWeather_getOneCallWithSameLatAndLon() = runBlockingTest {
        //given item of favItem
        viewModel.getCurrentWeather(favouriteLocation)
        //when get fav from response state
        delay(1000)
        viewModel.response.first()
        var x= (viewModel.response.first() as ResponseStateFav.SuccessApi).data
        //then check if fav equal null
        val res=(favouriteLocation.longitude ==x.lon.toString()&&favouriteLocation.latitude==x.lat.toString())
        MatcherAssert.assertThat(res, `is`(true))
    }
}

@ExperimentalCoroutinesApi
class MainCoroutinesRule(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }


}