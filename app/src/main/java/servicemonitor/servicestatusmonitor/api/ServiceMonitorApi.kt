package servicemonitor.servicestatusmonitor.api

import io.reactivex.Completable
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceMonitorApi {

    @GET("http://{url}")
    fun getStatus(
            @Path("url") url: String
    ): Completable

}