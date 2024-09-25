package com.serranoie.android.domain.model.action

sealed interface ErrorEntity {
    data class HttpError(val statusCode: Int, val message: String) : ErrorEntity
    data class NetworkError(val httpStatus: String) : ErrorEntity
    data class UnknownError(val message: String?) : ErrorEntity
    data object EmptyResponseError : ErrorEntity
}