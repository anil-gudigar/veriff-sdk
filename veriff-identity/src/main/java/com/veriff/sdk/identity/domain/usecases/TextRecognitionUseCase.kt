package com.veriff.sdk.identity.domain.usecases

import androidx.lifecycle.LiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.veriff.sdk.core.domain.IUseCase
import com.veriff.sdk.identity.data.repository.ITextRecognitionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextRecognitionUseCase @Inject constructor(private val repository: ITextRecognitionRepository) :
    IUseCase<TextRecognitionUseCase.Params, Text> {
    override suspend fun execute(params: Params): LiveData<Text> {
        return repository.detectInImage(params.image)
    }
    data class Params(val image: InputImage)
}