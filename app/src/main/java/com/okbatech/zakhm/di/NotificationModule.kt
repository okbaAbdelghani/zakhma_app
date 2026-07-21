package com.okbatech.zakhm.di

import android.content.Context
import androidx.work.WorkManager
import com.okbatech.zakhm.data.notification.AndroidNotificationService
import com.okbatech.zakhm.domain.notification.NotificationService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationService(impl: AndroidNotificationService): NotificationService

    companion object {
        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
            WorkManager.getInstance(context)
    }
}
