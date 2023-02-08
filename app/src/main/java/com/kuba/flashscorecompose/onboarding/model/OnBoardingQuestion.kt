package com.kuba.flashscorecompose.onboarding.model

import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 07/02/2023.
 */
sealed class OnBoardingQuestion(val titleId: Int) {
    object Teams : OnBoardingQuestion(R.string.team_on_boarding)
    object Players : OnBoardingQuestion(R.string.player)

}