package com.projects.reverbnews.module.error

enum class ErrorCodeMapper(
    val code: Int,
    val message: String,
    val lottieAsset: String
) {
    BadRequest(400, "Bad request. Please try again.", "bad_request.json"),
    Unauthorized(401, "Unauthorized access. Check your API key.", "unauthorized.json"),
    RequestOverForDay(403,"You've reached the daily request limit. Try again tomorrow.","too_many_requests.json"),
    ServerError(500, "Server error. Try later.", "server_error.json"),
    Network(0, "Network error. Check your connection.", "network_error.json"),
    Unknown(-1, "Something went wrong.", "unknown_error.json")
}

data class ErrorDetails(
    val message: String,
    val lottieAsset: String
)

fun mapErrorCodeToDetails(code: Int): ErrorDetails {
    val error = ErrorCodeMapper.values().firstOrNull { it.code == code } ?: ErrorCodeMapper.Unknown
    return ErrorDetails(message = error.message, lottieAsset = error.lottieAsset)
}