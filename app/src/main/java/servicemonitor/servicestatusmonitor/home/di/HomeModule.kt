package servicemonitor.servicestatusmonitor.home.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import servicemonitor.servicestatusmonitor.home.data.PollingService
import servicemonitor.servicestatusmonitor.home.ui.HomeActivity

@Module
abstract class HomeModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun contributePollingService(): PollingService

}

