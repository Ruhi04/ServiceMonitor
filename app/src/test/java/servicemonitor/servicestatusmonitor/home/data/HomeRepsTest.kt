package servicemonitor.servicestatusmonitor.home.data

import com.squareup.moshi.Moshi
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import servicemonitor.servicestatusmonitor.api.ServiceMonitorApi

class HomeRepoTest {
    @Rule
    @JvmField
    val mockServer = MockWebServer()
    private lateinit var repo: HomeRepo

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.from { it.run() } }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.from { it.run() } }

        val retrofit = Retrofit.Builder()
                .baseUrl(mockServer.url("/"))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(
                        Moshi.Builder()
                                .build()
                ))
                .build()

        repo = HomeRepo(retrofit.create(ServiceMonitorApi::class.java))
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun testGetServiceStatus() {
        //Given
        mockServer.enqueue(
                MockResponse()
                        .setBody("{}")
                        .setResponseCode(200)
        )

        //When
        var error: Throwable? = null
        repo.getServiceStatus("www.google.com").subscribeBy(
                onComplete = {
                },
                onError = {
                    error = it
                }
        )

        //Then
        Assertions.assertThat(error).isNull()
    }

    @Test
    fun testGetServiceStatusError() {
        //Given
        mockServer.enqueue(MockResponse().setResponseCode(400))

        // When
        var error: Throwable? = null
        repo.getServiceStatus("example").subscribeBy(
                onComplete = {
                },
                onError = {
                    error = it
                }
        )

        // Then
        Assertions.assertThat(error).isNotNull()
    }
}
