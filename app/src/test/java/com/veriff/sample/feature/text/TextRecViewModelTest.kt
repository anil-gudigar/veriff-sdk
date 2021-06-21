package com.veriff.sample.feature.text

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.mlkit.vision.text.Text
import com.veriff.sample.getOrAwaitValue
import com.veriff.sdk.identity.data.repository.FakeTextRecognitionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for the implementation of [FaceRecViewModel]
 *
 * @constructor Create empty Face rec viewmodel test
 */
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
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
    @Mock
    var value: Text? = null

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

        //TODO:MLKitContext is null so added a need to use Robolectric to mock MLKitContext
        image?.let {
            value = textRecViewModel.runTextRecognition(it).getOrAwaitValue()
        }
        //Then
        MatcherAssert.assertThat(
            value,
            CoreMatchers.not(CoreMatchers.nullValue())
        )

    }
}