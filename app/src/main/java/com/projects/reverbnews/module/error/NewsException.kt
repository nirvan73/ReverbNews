package com.projects.reverbnews.module.error

sealed class NewsException : Exception() {
    data class HttpError(val code: Int) : NewsException()
    object Network : NewsException()
    object Unknown : NewsException()
}