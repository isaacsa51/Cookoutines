package com.serranoie.android.domain.model.action

sealed interface Either<out S, out E> {
    data class Success<S>(val data: S) : Either<S, Nothing>
    data class Error<E>(val error: E) : Either<Nothing, E>
}