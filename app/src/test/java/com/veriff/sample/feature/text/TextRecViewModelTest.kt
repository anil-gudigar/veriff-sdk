package com.veriff.sample.feature.text

import android.content.Context
import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.veriff.sample.getOrAwaitValue
import com.veriff.sdk.identity.data.repository.FakeTextRecognitionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Unit tests for the implementation of [FaceRecViewModel]
 *
 * @constructor Create empty Face rec viewmodel test
 */
@ExperimentalCoroutinesApi
class TextRecViewModelTest {

    // Subject under test
    private lateinit var textRecViewModel: TextRecViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var textRecognitionRepository: FakeTextRecognitionRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    var image: Bitmap? = null // this creates a MUTABLE bitmap

    @Before
    fun setupViewModel() {
        MockitoAnnotations.initMocks(this)
        // We initialise the tasks to 3, with one active and two completed
        textRecognitionRepository = FakeTextRecognitionRepository()

        textRecViewModel = TextRecViewModel(textRecognitionRepository)
    }


    @Test
    fun runTextRecognition() = runBlockingTest {
        //GIVEN
        // Bitmap image
        // When adding a new task
        image?.let {
            textRecViewModel.runTextRecognition(it)
        }

        // Then the new task event is triggered
        val value = textRecViewModel.textRecData.getOrAwaitValue()

        MatcherAssert.assertThat(
            value.text,
            CoreMatchers.not(CoreMatchers.nullValue())
        )

    }
}