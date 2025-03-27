package com.mudrichenkoevgeny.movierating.model.data

data class Genre(
    val id: String,
    val name: String
)

fun getMockGenre() = Genre(
    id = "1",
    name = "Comedy"
)