package com.project.moviediscovery.movietest

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.moviediscovery.data.remote.api.ApiService
import com.project.moviediscovery.data.repo.MovieRepository
import com.project.moviediscovery.ui.detail.DetailActivity
import junit.framework.TestCase.assertTrue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import com.project.moviediscovery.utils.Result
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(DetailActivity::class.java);

    private var mockWebServer: MockWebServer = MockWebServer()

    private lateinit var apiService: ApiService
    private lateinit var movieRepository: MovieRepository

    private val client = OkHttpClient.Builder().build()
    private val contentType = "application/json".toMediaType()

    @Before
    fun setup() {
        apiService = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .client(client).addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)

        movieRepository = MovieRepository(apiService)
    }

    @Test
    fun whenErrorResponse() = runTest {
        val response = MockResponse()
            .setBody("The client messed this up")
            .setResponseCode(400)

        mockWebServer.enqueue(response)

        val flow = movieRepository.getMovieDetail(12)
        launch {
            flow.collect {
                if (it is Result.Error)
                    assertTrue(it.error == "HTTP 400 Client Error")
            }
        }
    }

    @Test
    fun whenMUnauthorized() = runTest {
        val response = MockResponse()
            .setBody("Unauthorized")
            .setResponseCode(401)

        mockWebServer.enqueue(response)

        val flow = movieRepository.getMovieDetail(12)
        launch {
            flow.collect {
                if (it is Result.Error)
                    assertFalse(it.error == "Unauthorized")
            }
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}