package com.okbatech.zakhm.presentation.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okbatech.zakhm.domain.usecase.GetAchievementsUseCase
import com.okbatech.zakhm.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val getAchievements: GetAchievementsUseCase,
    private val getUserProfile: GetUserProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AchievementsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(getUserProfile(), getAchievements()) { profile, achievements ->
                AchievementsState(profile = profile, achievements = achievements, isLoading = false)
            }.collect { _state.update { _ -> it } }
        }
    }
}
