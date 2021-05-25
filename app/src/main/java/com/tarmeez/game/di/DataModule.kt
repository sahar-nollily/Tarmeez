package com.tarmeez.game.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.tarmeez.game.data.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun providePreferenceManager(sharedPreferences: SharedPreferences):PreferenceManager =
        PreferenceManager(sharedPreferences)

    @Singleton
    @Provides
    fun provideSharedPreference (@ApplicationContext context: Context):SharedPreferences =
        EncryptedSharedPreferences.create("Taremeez",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
}