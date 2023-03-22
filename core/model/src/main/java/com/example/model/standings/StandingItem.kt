package com.example.model.standings

import com.example.model.team.Team

data class StandingItem(
    val all: InformationStanding,
    val away: InformationStanding,
    val home: InformationStanding,
    val selectedInformationStanding: InformationStanding,
    val description: String,
    val form: String,
    val goalsDiff: Int,
    val group: String,
    val points: Int,
    val rank: Int,
    val status: String,
    val team: Team,
    val update: String,
    val colorId: Int = 0
)
