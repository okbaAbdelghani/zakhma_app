package com.okbatech.zakhm.domain.usecase

import com.okbatech.zakhm.domain.model.UserProfile
import com.okbatech.zakhm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<UserProfile> = repository.getUserProfile()
}
