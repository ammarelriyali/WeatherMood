package com.example.weathermood.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weathermood.localdata.DAO
import com.example.weathermood.localdata.WeatherDB
import com.example.weathermood.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DAOTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: WeatherDB
    private lateinit var dao: DAO

    @Before
    fun start() {
        database =
            Room.inMemoryDatabaseBuilder(getApplicationContext(), WeatherDB::class.java).build()
        dao = database.getDao()
    }

    @Test
    fun getFavItems_FavItem_getListOfFavNotNull() = runBlockingTest {
        //given FavItem
        val fav = FavouriteLocation("l1", "l2", "cairo")
        dao.insertFav(fav)
        //when get task by id
        val test = dao.getFavItems().first()
        //then check if list is  not empty
        assertThat(test.isEmpty(), `is`(false))
    }

    @Test
    fun getFavItems_getListOfFavNull() = runBlockingTest {
        //when get fav
        val test = dao.getFavItems().first()
        //then check if list is empty
        assertThat(test.isEmpty(), `is`(true))
    }


    @Test
    fun isInsert_FavItem_getItemOfTheListNotNull() = runBlockingTest {
        //given FavItem and insert it
        val fav = FavouriteLocation("l1", "l2", "cairo")
        dao.insertFav(fav)
        //when get fav from list
        val test = dao.getFavItems().first()
        val find = test.find { it.latitude == fav.latitude && it.longitude == fav.longitude }
        //then check if fav is not null
        assertThat(find != null, `is`(true))
    }

    @Test
    fun isDelete_FavItem_getListWithoutItem() = runBlockingTest {
        //given FavItem and insert it then delete
        val fav = FavouriteLocation("l1", "l2", "cairo")
        dao.insertFav(fav)
        dao.deleteFav(fav)
        //when get fav from list
        val test = dao.getFavItems().first()
        val find = test.find { it.latitude == fav.latitude && it.longitude == fav.longitude }
        //then check if fav equal null
        assertThat(find != null, `is`(false))
    }


    @Test
    fun insertOneCall_OneCallHomeItem_getListWitOneCallHomeItem() = runBlockingTest {
        //given OneCallHome and insert it
        val oneCallHome = OneCallHome(
            id = 8936, oneCall = OneCall(
                current = Current(
                    clouds = 0.1,
                    dt = 5672,
                    feels_like = 2.3,
                    humidity = 4.5,
                    pressure = 6.7,
                    temp = 8.9,
                    weather = listOf(),
                    wind_speed = 10.11
                ), daily = listOf(), hourly = listOf(), lat = 12.13, lon = 14.15
            ), city = "Smeaton"
        )
        dao.insert(oneCallHome)
        //when get OneCallHome from list
        val test = dao.getCall().first()
        val find = test.find { it.id == oneCallHome.id }
        //then check if oneCallHome  equal null
        assertThat(find != null, `is`(true))
    }


    @Test
    fun insertAlert_AlertModelItem_getListWithAlertModelItem() = runBlockingTest {
        //given AlertModel and insert it
        val alertModel=AlertModel(
            dateForm = 3187,
            hourFrom = 2162,
            minuteFrom = 4639,
            dateTo = 1182,
            hourTo = 2877,
            minuteTo = 2614,
            lat = 16.17,
            log = 18.19,
            city = "Westlake Village",
            event = "tractatos",
            typeOfAlert = "delicata"
        )
        //when insert alertModel from list
        val test=dao.insertAlert(alertModel)
        //then check if alertModel equal null
        assertThat(test, `not`(-1))
    }

    @After
    fun clear() {

        database.close()
    }

}