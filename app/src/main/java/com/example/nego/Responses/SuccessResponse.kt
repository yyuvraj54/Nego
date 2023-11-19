package com.example.nego.Responses

data class SuccessResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val output: String,
    val safetyRatings: List<SafetyRating>
)

data class SafetyRating(
    val category: String,
    val probability: String
)