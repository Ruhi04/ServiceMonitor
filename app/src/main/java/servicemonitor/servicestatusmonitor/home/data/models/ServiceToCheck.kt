package servicemonitor.servicestatusmonitor.home.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ServiceToCheck(
        @PrimaryKey
        var id: String? = null,
        var serviceName: String? = null,
        var serviceUrl: String? = null,
        var status: String? = null,
        var lastChecked: String? = null

) : RealmObject() {
}
