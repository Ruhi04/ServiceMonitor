package servicemonitor.servicestatusmonitor.home.data.dao

import io.realm.Realm
import servicemonitor.servicestatusmonitor.home.data.models.RealmLiveData
import servicemonitor.servicestatusmonitor.home.data.models.ServiceToCheck
import java.util.*
import javax.inject.Inject


class ServicesDao @Inject constructor(private val db: Realm) {

    fun addService(serviceName: String?, serviceUrl: String?) {
        db.executeTransaction { realm ->
            val service = realm.createObject(ServiceToCheck::class.java, UUID.randomUUID().toString())
            service.serviceName = serviceName
            service.serviceUrl = serviceUrl
            service.status = "N/A"
            service.lastChecked = "N/A"
        }
    }

    fun updateServiceStatus(serviceUrl: String, status: String, lastChecked: String?) {
        val services = db.where(ServiceToCheck::class.java)
                .equalTo("serviceUrl", serviceUrl)
                .findAll()
        services?.forEach {
            db.executeTransaction { realm ->
                it.status = status
                it.lastChecked = lastChecked
                realm.copyToRealmOrUpdate(it)
            }
        }
    }

    fun findAll(): RealmLiveData<ServiceToCheck> {
        return db.where(ServiceToCheck::class.java)
                .findAllAsync()
                .asLiveData()
    }

    fun findAllSync(): RealmLiveData<ServiceToCheck> {
        return db.where(ServiceToCheck::class.java)
                .findAll()
                .asLiveData()
    }

    //Not Used Yet
    fun delete(id: String?) {
        db.executeTransaction { _ ->
            val result = db.where(ServiceToCheck::class.java)
                    .equalTo("id", id)
                    .findAll()
            result.deleteAllFromRealm()
        }
    }
}
