package com.skintker

import com.skintker.data.components.InputValidator
import com.skintker.data.components.PaginationManager
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

    val mockedInputValidator = mockk<InputValidator>()

    val mockedPaginationManager = mockk<PaginationManager>()

    val mockedStatsRepository = mockk<StatsRepository>()

    val mockedReportsRepository = mockk<ReportsRepository>()


    fun ApplicationTestBuilder.configureClient() = createClient {
        with(this@configureClient) {
            application {
                configureKoin()
                configureRouting(mockedInputValidator,mockedPaginationManager,mockedStatsRepository,mockedReportsRepository)
            }
            install(ContentNegotiation) { json() }
        }
    }

}