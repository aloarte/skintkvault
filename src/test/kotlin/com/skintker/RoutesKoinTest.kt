package com.skintker

import com.skintker.data.validators.InputValidator
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.repository.StatsRepository
import com.skintker.plugins.configureKoin
import com.skintker.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import io.mockk.mockk
import org.koin.test.KoinTest

abstract class RoutesKoinTest : KoinTest {

    val mockedStatsRepository = mockk<StatsRepository>()

    val mockedReportsRepository = mockk<ReportsRepository>()

    val mockedInputValidator = mockk<InputValidator>()


    fun ApplicationTestBuilder.configureClient() = createClient {
        with(this@configureClient) {
            application {
                configureKoin()
                configureRouting(mockedStatsRepository,mockedReportsRepository, mockedInputValidator)
            }
            install(ContentNegotiation) { json() }
        }
    }

}