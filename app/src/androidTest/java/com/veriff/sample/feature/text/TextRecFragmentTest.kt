package com.veriff.sample.feature.text

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.veriff.sample.R
import com.veriff.sample.ServiceLocator
import com.veriff.sample.sdk.identity.data.repository.local.text.FakeAndroidTextRecognitionRepository
import com.veriff.sdk.identity.data.repository.ITextRecognitionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TextRecFragmentTest {
    private lateinit var repository: ITextRecognitionRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTextRecognitionRepository()
        ServiceLocator.textRecRepository = repository
    }

    @Test
    fun test_DisplayedInUi() = runBlockingTest{
        // GIVEN - Add active (incomplete) task to the DB
        val scenario = launchFragmentInContainer<TextRecFragment>(Bundle(), R.style.Base_Theme_AppCompat)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        // WHEN - Details fragment launched to display task

        // THEN - Task details are displayed on the screen
        // make sure that the title/description are both shown and correct
        Espresso.onView(ViewMatchers.withId(R.id.veriffScannerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.camera_selector))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.camera_shutter))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}