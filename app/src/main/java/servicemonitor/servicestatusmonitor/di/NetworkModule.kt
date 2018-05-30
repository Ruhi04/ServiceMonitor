package servicemonitor.servicestatusmonitor.di

import android.app.Application
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.realm.Realm
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import servicemonitor.servicestatusmonitor.R
import servicemonitor.servicestatusmonitor.api.ServiceMonitorApi
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient
                    .Builder()
                    .build()

    @Provides
    @Singleton
    fun provideMoshi() = Moshi.Builder()
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(application: Application, client: OkHttpClient, moshi: Moshi) = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .baseUrl(application.getString(R.string.base_url))
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ServiceMonitorApi = retrofit.create(ServiceMonitorApi::class.java)

    @Provides
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }
}
