package com.example.weatherapp.data.repository

import com.example.weatherapp.api.NetworkResult
import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.data.models.geo.GeoModel
import com.example.weatherapp.data.models.weather.WeatherModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.IOException

// Simple unit test to demonstrate correct network result given
// As addition, I would test particulars about exact messages and data sent as part of the network result
@RunWith(MockitoJUnitRunner::class)
class WeatherDataRepositoryTest {
    private lateinit var repository: WeatherDataRepository

    @Mock
    lateinit var weatherApiService: WeatherApiService

    @Mock
    lateinit var localCityDataSource: LocationNameCache

    @Before
    fun setup() {
        repository = WeatherDataRepository(weatherApiService, localCityDataSource)
    }

    @Test
    fun `test getWeatherData with successful response`() = runBlocking {
        val weatherModel = mock(WeatherModel::class.java)
        val response = Response.success(weatherModel)
        val lat = 1.23
        val lon = 4.56

        `when`(weatherApiService.getWeatherData(lat, lon, "")).thenReturn(response)

        val result = repository.getWeatherData(lat, lon)

        assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
    }

    @Test
    fun `test getWeatherData with error response`() = runBlocking {
        val response = Response.error<WeatherModel>(
            404,
            ResponseBody.create(MediaType.get("application/json"), "not found")
        )
        val lat = 1.23
        val lon = 4.56

        `when`(weatherApiService.getWeatherData(lat, lon, "")).thenReturn(response)

        val result = repository.getWeatherData(lat, lon)

        assertThat(result).isInstanceOf(NetworkResult.Error::class.java)
    }

    @Test
    fun `test getWeatherData with network error`() = runBlocking {
        val lat = 1.23
        val lon = 4.56

        `when`(weatherApiService.getWeatherData(lat, lon, "")).thenAnswer { IOException() }

        val result = repository.getWeatherData(lat, lon)

        assertThat(result).isInstanceOf(
            NetworkResult.Error::class.java
        )
    }

    @Test
    fun `test getGeoCoordinates with successful response`() = runBlocking {
        val geoModel = mock(GeoModel::class.java)
        val response = Response.success(geoModel)
        val cityName = "New York"

        `when`(weatherApiService.getLocationCoordinates(cityName)).thenReturn(response)

        val result = repository.getGeoCoordinates(cityName)

        assertThat(result).isInstanceOf(NetworkResult.Success::class.java)
    }

    @Test
    fun `test getGeoCoordinates with error response`() = runBlocking {
        val response = Response.error<GeoModel>(
            404,
            ResponseBody.create(MediaType.get("application/json"), "not found")
        )
        val cityName = "New York"

        `when`(weatherApiService.getLocationCoordinates(cityName)).thenReturn(response)

        val result = repository.getGeoCoordinates(cityName)

        assertThat(result).isInstanceOf(NetworkResult.Error::class.java)
    }

    @Test
    fun `test getGeoCoordinates with network error`() = runBlocking {
        val cityName = "New York"

        `when`(weatherApiService.getLocationCoordinates(cityName)).thenAnswer { throw IOException() }

        val exception = try {
            repository.getGeoCoordinates(cityName)
        } catch (exception: RuntimeException) {
            exception
        }

        assertThat(exception).isInstanceOf(NetworkResult.Error::class.java)
    }
}
