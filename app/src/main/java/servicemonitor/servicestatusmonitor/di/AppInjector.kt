package servicemonitor.servicestatusmonitor.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import servicemonitor.servicestatusmonitor.ServiceMonitorApplication
import servicemonitor.servicestatusmonitor.home.di.HomeModule
import javax.inject.Singleton

@Component(
        modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        HomeModule::class,
        ViewModelModule::class,
        NetworkModule::class
        ]
)
@Singleton
interface AppInjector {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppInjector
    }

    fun inject(app: ServiceMonitorApplication)
}

