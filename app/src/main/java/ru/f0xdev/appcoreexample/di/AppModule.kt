package ru.f0xdev.appcoreexample.di

import android.accounts.AccountManager
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.f0xdev.appcoreexample.BuildConfig
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.appcoreexample.net.HttpHeaderInterceptor
import ru.f0xdev.f0xcore.auth.*
import ru.f0xdev.f0xcore.auth.net.UnauthorizedErrorProcessor
import ru.f0xdev.f0xcore.net.UserAgentProvider
import ru.f0xdev.f0xcore.util.Cryptographer


const val CONNECT_TIMEOUT_SEC = 60L
const val READ_TIMEOUT_SEC = 60L

val commonModule = module {

    single<ITokenCryptographer> {
        TokenCryptographer(Cryptographer())
    }

    single<IAuthManager> {
        val context: Context = get()
        val authConfig = AuthConfig(
            context.getString(R.string.account_type),
            context.getString(R.string.app_name),
            "", ""
        )
        AuthManager(
            AccountManager.get(context),
            authConfig,
            get()
        )
    }

    factory {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
    }
}

fun networkModule(baseUrl: String) = module {

    single {
        val gson: Gson = get()

        val userAgentProvider = UserAgentProvider(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME)
        val unauthorizedErrorProcessor = UnauthorizedErrorProcessor(get())
        val httpErrorProcessors = mapOf(
            UnauthorizedErrorProcessor.HTTP_CODE to unauthorizedErrorProcessor
        )
        val headerInterceptor = HttpHeaderInterceptor(
            userAgentProvider,
            get(),
            httpErrorProcessors
        )
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(headerInterceptor)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        val client = builder.build()
        unauthorizedErrorProcessor.okHttpClient = client

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }


}