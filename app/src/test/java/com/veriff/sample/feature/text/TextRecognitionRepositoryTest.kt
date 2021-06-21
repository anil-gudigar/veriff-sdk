package com.veriff.sample.feature.text

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.mlkit.vision.common.InputImage
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
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Text recognition repository test
 *
 * @constructor Create empty Text recognition repository test
 */
@ExperimentalCoroutinesApi
class TextRecognitionRepositoryTest {
    // Use a fake repository to be injected into the viewmodel
    private lateinit var textRecognitionRepository: FakeTextRecognitionRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    var image: Bitmap? = null // this creates a MUTABLE bitmap
    @Mock
    val inputImage: InputImage? = null // this creates a MUTABLE InputImage

    @Before
    fun setupViewModel() {
        MockitoAnnotations.initMocks(this)
        // We initialise the tasks to 3, with one active and two completed
        textRecognitionRepository = FakeTextRecognitionRepository()
    }


    @Test
    fun runTextRecognition() = runBlockingTest {
        //GIVEN
        var value: Text? = null
        // Bitmap image
        // When adding a new task
        inputImage?.let {
            value =  textRecognitionRepository.detectInImage(it).getOrAwaitValue()
        }

        //Then
        MatcherAssert.assertThat(
            value,
            CoreMatchers.not(CoreMatchers.nullValue())
        )

    }
}