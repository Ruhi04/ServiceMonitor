package servicemonitor.servicestatusmonitor.home.data.models

import android.arch.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmResults

class RealmLiveData<T : ServiceToCheck>(val realmResults: RealmResults<T>) : LiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results -> value = results }

    override fun onActive() {
        realmResults.addChangeListener(listener)
    }

    override fun onInactive() {
        realmResults.removeChangeListener(listener)
    }
}
