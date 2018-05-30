package servicemonitor.servicestatusmonitor

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.res.Resources
import android.support.v7.app.AppCompatDelegate
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import io.realm.Realm
import net.danlew.android.joda.JodaTimeAndroid
import servicemonitor.servicestatusmonitor.di.DaggerAppInjector
import java.util.*
import javax.inject.Inject

class ServiceMonitorApplication : Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingAndroidServiceInjector: DispatchingAndroidInjector<Service>

    override fun onCreate() {
        super.onCreate()

        DaggerAppInjector.builder()
                .application(this)
                .build()
                .inject(this)

        setUpJodaTime()
        Realm.init(this)
        resources.configuration.setLocale(Locale("sv", "SE"))
    }

    private fun setUpJodaTime() {
        try {
            JodaTimeAndroid.init(this)
        } catch (ex: Resources.NotFoundException) {
        }
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun serviceInjector(): AndroidInjector<Service> = dispatchingAndroidServiceInjector

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}

