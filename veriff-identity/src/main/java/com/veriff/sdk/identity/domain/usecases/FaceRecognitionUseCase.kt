package com.veriff.sdk.identity.domain.usecases

import androidx.lifecycle.LiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.veriff.sdk.core.domain.IUseCase
import com.veriff.sdk.identity.data.repository.IFaceRecognitionRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Face recognition use case
 *
 * @property repository
 * @constructor Create empty Face recognition use case
 */
@Singleton
class FaceRecognitionUseCase @Inject constructor(private val repository: IFaceRecognitionRepository) :
    IUseCase<FaceRecognitionUseCase.Params, List<Face>> {
    /**
     * Execute
     *
     * @param params
     * @return
     */
    override suspend fun execute(params: Params): LiveData<List<Face>> {
        return repository.detectInImage(params.image)
    }
    data class Params(val image: InputImage)
}