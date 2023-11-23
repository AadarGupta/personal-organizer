package com.example.models.http

import kotlinx.serialization.Serializable


@Serializable
data class UserSignupRequest(
    var username: String,
    var password: String
)

@Serializable
data class UserLoginRequest(
    var username: String,
    var password: String
)


