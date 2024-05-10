package com.project.moviediscovery.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.moviediscovery.data.repo.AuthRepository
import com.project.moviediscovery.ui.auth.AuthViewModel
import com.project.moviediscovery.utils.CoroutineTestRule
import com.project.moviediscovery.utils.LiveDataTestUtils.getOrAwaitValue
import com.project.moviediscovery.utils.UserPreferences
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var userPreferences: UserPreferences

    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        authViewModel = AuthViewModel(authRepository, userPreferences)
    }

    @Test
    fun `when Successfully Save Preferences`() = runTest {
        val accessToken = true
        val email = "email"
        val profilePic = "profilePic"

        authViewModel.savePreferences(accessToken, email, profilePic)
        Mockito.verify(userPreferences)
            .savePreferences(accessToken, email, profilePic)
    }

    @Test
    fun `when Successfully Get isLoggedIn`(): Unit = runTest {
        val isLoggedIn = true
        val expectedResult = flowOf(isLoggedIn)

        Mockito.`when`(userPreferences.getIsLoggedIn()).thenReturn(expectedResult)

        val actualResult = authViewModel.getIsLoggedIn().getOrAwaitValue()

        TestCase.assertEquals(isLoggedIn, actualResult)
        Mockito.verify(userPreferences).getIsLoggedIn()
    }
}