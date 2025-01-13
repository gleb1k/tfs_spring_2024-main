package ru.glebik.core.network.interceptors

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val newRequest = chain.request().newBuilder()
            .addHeader(AUTHORIZATION_HEADER, Credentials.basic(EMAIL, API_KEY))
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val EMAIL = "gleb.gafeev@mail.ru"
        private const val API_KEY = "RuZUUuOHyb6v9FfGbEGlOO3eCKmh0l5Q"
        private const val AUTHORIZATION_HEADER = "Authorization"
    }
}