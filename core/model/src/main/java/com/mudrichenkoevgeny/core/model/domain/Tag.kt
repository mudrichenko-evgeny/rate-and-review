package com.mudrichenkoevgeny.core.model.domain

data class Tag(
    val id: String,
    val name: String
)

fun getMockTag() = Tag(
    id = "1",
    name = "Funny"
)