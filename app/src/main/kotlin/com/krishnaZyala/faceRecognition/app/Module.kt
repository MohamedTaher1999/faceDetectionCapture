package com.krishnaZyala.faceRecognition.app

import android.app.Application
import com.krishnaZyala.faceRecognition.data.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideFaceRepo(app: Application): Repository = Repository(app)

}