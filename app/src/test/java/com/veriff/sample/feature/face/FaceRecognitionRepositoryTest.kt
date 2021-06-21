package com.veriff.sample.feature.face

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.veriff.sample.getOrAwaitValue
import com.veriff.sdk.identity.data.repository.FakeFaceRecognitionRepository
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
 * Face recognition repository test
 *
 * @constructor Create empty Face recognition repository test
 */
@ExperimentalCoroutinesApi
class FaceRecognitionRepositoryTest {

    // Use a fake repository to be injected into the viewmodel
    private lateinit var faceRecognitionRepository: FakeFaceRecognitionRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    val image: Bitmap? = null // this creates a MUTABLE bitmap
    @Mock
    val inputImage: InputImage? = null // this creates a MUTABLE InputImage


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        // We initialise the tasks to 3, with one active and two completed
        faceRecognitionRepository = FakeFaceRecognitionRepository()
    }

    @Test
    fun runFaceRecognition() = runBlockingTest {
        //GIVEN
        // Bitmap image
        var value :List<Face> ?= null
        // When adding a new task
        inputImage?.let {
            value =  faceRecognitionRepository.detectInImage(it).getOrAwaitValue()
        }

        //Then
        MatcherAssert.assertThat(
            value?.size,
            CoreMatchers.not(CoreMatchers.nullValue())
        )
    }
}