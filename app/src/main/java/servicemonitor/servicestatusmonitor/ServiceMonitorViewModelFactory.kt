package servicemonitor.servicestatusmonitor

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ServiceMonitorViewModelFactory @Inject constructor(
        private val creators: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    /**
     * Create a new view model.
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator = creators[modelClass]

        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }

        if (creator == null) {
            throw IllegalArgumentException("Unknown model class $modelClass")
        }

        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (ex: Exception) {
            throw RuntimeException("Could not create view model", ex)
        }
    }
}

