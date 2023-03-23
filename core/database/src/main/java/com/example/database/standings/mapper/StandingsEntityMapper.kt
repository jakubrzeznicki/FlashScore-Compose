package com.example.database.standings.mapper

import com.example.database.leagues.mapper.toLeague
import com.example.database.standings.model.GoalsStandingEntity
import com.example.database.standings.model.InformationStandingEntity
import com.example.database.standings.model.StandingItemEntity
import com.example.database.standings.model.StandingsEntity
import com.example.database.teams.mapper.toTeam
import com.example.model.standings.GoalsStanding
import com.example.model.standings.InformationStanding
import com.example.model.standings.Standing
import com.example.model.standings.StandingItem

/**
 * Created by jrzeznicki on 13/03/2023.
 */
fun StandingsEntity.toStandings(): Standing {
    return Standing(
        league = league.toLeague(),
        leagueId = leagueId,
        season = season,
        standingItems = standings.map { it.toStandingItem() }
    )
}

fun StandingItemEntity.toStandingItem(): StandingItem {
    return StandingItem(
        all = all.toInformationStanding(),
        away = away.toInformationStanding(),
        home = home.toInformationStanding(),
        selectedInformationStanding = all.toInformationStanding(),
        description = description,
        form = form,
        goalsDiff = goalsDiff,
        group = group,
        points = points,
        rank = rank,
        status = status,
        team = team.toTeam(),
        update = update
    )
}

fun InformationStandingEntity.toInformationStanding(): InformationStanding {
    return InformationStanding(
        draw = draw,
        goals = goals.toGoalsStanding(),
        lose = lose,
        played = played,
        win = win
    )
}

fun GoalsStandingEntity.toGoalsStanding(): GoalsStanding {
    return GoalsStanding(against = against, forValue = forValue)
}