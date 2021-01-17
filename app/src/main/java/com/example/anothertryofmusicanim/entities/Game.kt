package com.example.anothertryofmusicanim.entities

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val name : String,
    val stations : List<Radio>
)