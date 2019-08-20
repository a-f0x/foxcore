package ru.f0xdev.appcoreexample.di

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.f0xdev.appcoreexample.BuildConfig
import ru.f0xdev.appcoreexample.auth.AuthRemoteDataSource
import ru.f0xdev.appcoreexample.auth.AuthRemoteDataSourceRetrofit
import ru.f0xdev.f0xcore.auth.ITokenCryptographer
import ru.f0xdev.f0xcore.auth.di.AuthServiceDependencies
import ru.f0xdev.f0xcore.auth.net.IAuthRemoteDataSource
import java.util.concurrent.TimeUnit

open class AuthServiceDependenciesKoin(
    private val gson: Gson,
    private val cryptographer: ITokenCryptographer,
    private val baseUrl: String
) :
    AuthServiceDependencies {

    override fun provideITokenCryptographer(): ITokenCryptographer = cryptographer

    private val retrofit: Retrofit by lazy {

        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        val client = builder.build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

    }

    override fun provideAuthRemoteDataSource(): IAuthRemoteDataSource {
        return AuthRemoteDataSource(
            retrofit.create(AuthRemoteDataSourceRetrofit::class.java)
        )
    }
}