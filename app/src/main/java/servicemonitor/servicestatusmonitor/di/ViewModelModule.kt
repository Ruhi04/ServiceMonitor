package servicemonitor.servicestatusmonitor.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import servicemonitor.servicestatusmonitor.ServiceMonitorViewModelFactory
import servicemonitor.servicestatusmonitor.home.ui.HomeViewModel
import javax.inject.Singleton

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindSetupHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ServiceMonitorViewModelFactory): ViewModelProvider.Factory

}