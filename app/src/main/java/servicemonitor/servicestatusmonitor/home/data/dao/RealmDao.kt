package servicemonitor.servicestatusmonitor.home.data.dao

import io.realm.Realm
import io.realm.RealmResults
import servicemonitor.servicestatusmonitor.home.data.models.RealmLiveData
import servicemonitor.servicestatusmonitor.home.data.models.ServiceToCheck

fun <T : ServiceToCheck> RealmResults<T>.asLiveData() = RealmLiveData(this)
fun Realm.servicesDao(): ServicesDao = ServicesDao(this)