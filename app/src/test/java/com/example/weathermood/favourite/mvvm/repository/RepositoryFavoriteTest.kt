package com.example.weathermood.favourite.mvvm.repository

import com.example.weathermood.model.FavouriteLocation
import com.example.weathermood.model.OneCall
import com.example.weathermood.favourite.mvvm.repository.repoitory.FakeLocalData
import com.example.weathermood.favourite.mvvm.repository.repoitory.FakeRemotelyData
import com.example.weathermood.model.Current
import com.example.weathermood.model.OneCallHome
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RepositoryFavoriteTest {

    private var repositoryFavorite: RepositoryFavorite? = null
    private val list: List<FavouriteLocation> = listOf(
        FavouriteLocation(
            longitude = "disputationi",
            latitude = "singulis",
            city = "Mos Eisley"
        ),
        FavouriteLocation(longitude = "erroribus", latitude = "montes", city = "Noordeloos"),
        FavouriteLocation(longitude = "mandamus", latitude = "omittantur", city = "Saturn"),
        FavouriteLocation(longitude = "ponderum", latitude = "suscipit", city = "Zion"),
        FavouriteLocation(longitude = "equidem", latitude = "unum", city = "Priest")


    )
    private val oneCallHome = OneCallHome(
        id = 9934, oneCall = OneCall(
            current = Current(
                clouds = 20.21,
                dt = 8209,
                feels_like = 22.23,
                humidity = 24.25,
                pressure = 26.27,
                temp = 28.29,
                weather = listOf(),
                wind_speed = 30.31
            ), daily = listOf(), hourly = listOf(), lat = 32.33, lon = 34.35
        ), city = "Los Santos"
    )

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutinesRule = MainCoroutinesRule()

    @Before
    fun setUp() {
        repositoryFavorite =
            RepositoryFavorite(
                FakeLocalData(mutableListOf()),
                FakeRemotelyData(oneCall = oneCallHome)
            )
    }

    @After
    fun tearDown() {
        repositoryFavorite = null
    }

    @Test
    fun getFavItems_getSizeOfList() = runBlockingTest {
        //when get fav from list
        val arr = repositoryFavorite?.getFavItems()?.first()
        //then check if fav equal null
        MatcherAssert.assertThat(arr?.size, `is`(0))
    }

    @Test
    fun getCurrentLocation() = runBlockingTest {
        //given  get item from current location
        val res = repositoryFavorite?.getCurrentLocation(
            oneCallHome.oneCall.lon.toString(),
            oneCallHome.oneCall.lat.toString()
        )
        //when one call have same lat and lon
        val oneCall = oneCallHome.oneCall.apply {
            this.lat = lat
            this.lon = lon
        }
        //then check if onCall equal to result of getCurrentLocation()
        val test = res?.first()?.body()
        MatcherAssert.assertThat(test?.lat, `is`(oneCall.lat))
    }

    @Test
    fun insertFav_FavItem_checkIsInserted() = runBlockingTest {
        //given  insert item
        repositoryFavorite?.insertFav(list[0])
        //when
        //then check if item that added have the same data
        val res = repositoryFavorite?.getFavItems()?.first()
        val b = res?.find { it.latitude == list[0].latitude && it.longitude == list[0].longitude }
        MatcherAssert.assertThat(b?.latitude, `is`(list[0].latitude))
    }

    @Test
    fun deleteFavItem_FavItem_checkIsDelete() = runBlockingTest {
        //given  insert item
        repositoryFavorite?.insertFav(list[0])
        //when delete the item
        repositoryFavorite?.deleteFavItem(list[0])
        //then check if item delete and not find in list
        val res = repositoryFavorite?.getFavItems()?.first()
        val b = res?.find { it.latitude == list[0].latitude && it.longitude == list[0].longitude } == null
        MatcherAssert.assertThat(b, `is`(true))
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