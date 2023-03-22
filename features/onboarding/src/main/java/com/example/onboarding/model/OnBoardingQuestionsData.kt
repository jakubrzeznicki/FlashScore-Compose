package com.example.onboarding.model

/**
 * Created by jrzeznicki on 07/02/2023.
 */
data class OnBoardingQuestionsData(
    val questionIndex: Int = 0,
    val questionCount: Int = 2,
    val shouldShowPreviousButton: Boolean = false,
    val isNextEnabledButton: Boolean = false,
    val shouldShowDoneButton: Boolean = false,
    val onBoardingQuestion: OnBoardingQuestion = OnBoardingQuestion.Teams
)
