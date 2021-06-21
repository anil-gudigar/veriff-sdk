package com.veriff.sample.feature.face

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
 * Unit tests for the implementation of [FaceRecViewModel]
 *
 * @constructor Create empty Face rec viewmodel test
 */
@ExperimentalCoroutinesApi
class FaceRecViewModelTest {

    // Subject under test
    private lateinit var faceRecViewModel: FaceRecViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var faceRecognitionRepository: FakeFaceRecognitionRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    val image:Bitmap ? = null // this creates a MUTABLE bitmap


    @Before
    fun setupViewModel() {
        MockitoAnnotations.initMocks(this)
        // We initialise the tasks to 3, with one active and two completed
        faceRecognitionRepository = FakeFaceRecognitionRepository()
        faceRecViewModel = FaceRecViewModel(faceRecognitionRepository)
    }

    @Test
    fun runFaceRecognition() = runBlockingTest {
        //GIVEN
        var value :List<Face> ?= null
        // Bitmap image
        // When adding a new task
        image?.let {
            value = faceRecViewModel.runFaceDetection(it).getOrAwaitValue()
        }
        //Then
        MatcherAssert.assertThat(
            value?.size,
            CoreMatchers.not(CoreMatchers.nullValue())
        )
    }
}