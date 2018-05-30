package servicemonitor.servicestatusmonitor.home.ui

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.rxkotlin.subscribeBy
import io.realm.Realm
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import servicemonitor.servicestatusmonitor.home.data.HomeRepo
import servicemonitor.servicestatusmonitor.home.data.dao.servicesDao
import servicemonitor.servicestatusmonitor.home.data.models.RealmLiveData
import servicemonitor.servicestatusmonitor.home.data.models.ServiceToCheck
import javax.inject.Inject

class HomeViewModel @Inject constructor(
        private val repo: HomeRepo
) : ViewModel() {

    val serviceName = ObservableField<String>()
    val serviceUrl = ObservableField<String>()
    private val timeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss")

    @Inject
    internal lateinit var realm: Realm

    fun getServicesList(): RealmLiveData<ServiceToCheck> {
        return realm.servicesDao().findAll()
    }

    fun addService() {
        realm.servicesDao().addService(serviceName.get(), serviceUrl.get())
        updateNewService(serviceUrl.get())
    }

    private fun updateNewService(serviceUrl: String) {
        repo.getServiceStatus(serviceUrl)
                .subscribeBy(
                        onComplete = {
                            realm.servicesDao().updateServiceStatus(
                                    serviceUrl,
                                    "OK",
                                    timeFormatter.print(DateTime.now())
                            )
                        },
                        onError = {
                            realm.servicesDao().updateServiceStatus(
                                    serviceUrl,
                                    "FAIL",
                                    timeFormatter.print(DateTime.now())
                            )
                        }
                )
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

}


