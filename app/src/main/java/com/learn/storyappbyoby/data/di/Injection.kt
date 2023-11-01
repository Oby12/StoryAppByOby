package com.learn.storyappbyoby.data.di

import android.content.Context
import com.learn.storyappbyoby.data.UserRepository
import com.learn.storyappbyoby.data.api.ApiConfig
import com.learn.storyappbyoby.data.api.ApiService
import com.learn.storyappbyoby.data.pref.UserPreference
import com.learn.storyappbyoby.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService,pref)
    }

}