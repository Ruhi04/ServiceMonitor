package servicemonitor.servicestatusmonitor.home.data

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.android.AndroidInjection
import io.reactivex.rxkotlin.subscribeBy
import io.realm.Realm
import io.realm.RealmResults
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import servicemonitor.servicestatusmonitor.home.data.dao.servicesDao
import servicemonitor.servicestatusmonitor.home.data.models.ServiceToCheck
import javax.inject.Inject

class PollingService : Service() {

    @Inject
    internal lateinit var repo: HomeRepo

    @Inject
    internal lateinit var realm: Realm

    private val timeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss")

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val services = getServicesListFromDb()

        services?.forEach { service ->
            repo.getServiceStatus(service.serviceUrl ?: "")
                    .subscribeBy(
                            onComplete = {
                                updateDb(service.serviceUrl ?: "", "OK", timeFormatter.print(DateTime.now()))
                            },
                            onError = {
                                updateDb(service.serviceUrl ?: "", "FAIL", timeFormatter.print(DateTime.now()))
                            }
                    )
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun getServicesListFromDb(): RealmResults<ServiceToCheck>? {
        return realm.servicesDao().findAllSync().realmResults
    }

    private fun updateDb(serviceUrl: String, serviceStatus: String, lastChecked: String) {
        realm.servicesDao().updateServiceStatus(serviceUrl, serviceStatus, lastChecked)
    }

}