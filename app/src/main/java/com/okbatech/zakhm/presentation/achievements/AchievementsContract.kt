package com.okbatech.zakhm.presentation.achievements

import com.okbatech.zakhm.domain.model.Achievement
import com.okbatech.zakhm.domain.model.UserProfile

data class AchievementsState(
    val profile: UserProfile = UserProfile(),
    val achievements: List<Achievement> = emptyList(),
    val isLoading: Boolean = true
)

sealed class AchievementsEvent {
    data object Refresh : AchievementsEvent()
}
