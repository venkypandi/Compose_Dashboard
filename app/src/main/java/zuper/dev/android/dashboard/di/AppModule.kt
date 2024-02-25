package zuper.dev.android.dashboard.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import zuper.dev.android.dashboard.data.remote.ApiDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiDataSource():ApiDataSource{
        return ApiDataSource()
    }
}