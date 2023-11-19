package com.example.nego.Responses

data class ErrorResponse(
    val error: ErrorDetails
)

data class ErrorDetails(
    val code: Int,
    val message: String,
    val status: String,
    val details: List<ErrorDetail>
)

data class ErrorDetail(
    val type: String,
    val fieldViolations: List<FieldViolation>
)

data class FieldViolation(
    val description: String
)
