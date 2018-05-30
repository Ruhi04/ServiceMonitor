package servicemonitor.servicestatusmonitor.home.data

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import servicemonitor.servicestatusmonitor.api.ServiceMonitorApi
import javax.inject.Inject

class HomeRepo @Inject constructor(private val api: ServiceMonitorApi) {

    fun getServiceStatus(url: String): Completable =
            api.getStatus(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}