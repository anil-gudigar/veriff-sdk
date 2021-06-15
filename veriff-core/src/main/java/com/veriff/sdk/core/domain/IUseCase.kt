package com.veriff.sdk.core.domain

import androidx.lifecycle.LiveData

interface IUseCase<in I : Any, O : Any> {
    suspend fun execute(input: I): LiveData<O>
}