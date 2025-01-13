package ru.glebik.tinkoff_fintech.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.glebik.core.network.interceptors.AuthInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object TestNetworkModule {

    @Provides
    @Singleton
    fun provideKotlinxSerializer(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(authInterceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        kotlinxSerializer: Json,
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(
                kotlinxSerializer.asConverterFactory(
                    CONTENT_TYPE.toMediaType()
                )
            )
            .build()

    private const val BASE_URL = "http://localhost:8080/"
    private const val CONTENT_TYPE = "application/json; charset=UTF8"

}