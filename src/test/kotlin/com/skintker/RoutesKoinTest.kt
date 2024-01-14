package com.skintker

import com.skintker.data.components.InputValidator
import com.skintker.data.components.PaginationManager
import com.skintker.domain.repository.ReportsRepository
import com.skintker.domain.repository.StatsRepository
import com.skintker.plugins.configureKoin
import com.skintker.plugins.configureRouting
import com.skintker.domain.UserManager
import com.skintker.domain.repository.UserRepository
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.ApplicationTestBuilder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import org.koin.test.KoinTest

open class RoutesKoinTest : KoinTest {

    val mockedInputValidator = mockk<InputValidator>()
    val mockedUserManager = mockk<UserManager>()
    val mockedPaginationManager = mockk<PaginationManager>()
    val mockedStatsRepository = mockk<StatsRepository>()
    val mockedReportsRepository = mockk<ReportsRepository>()

    fun ApplicationTestBuilder.configureClient() = createClient {
        with(this@configureClient) {
            application {
                configureKoin()
                configureRouting(
                    mockedInputValidator,
                    mockedUserManager,
                    mockedPaginationManager,
                    mockedStatsRepository,
                    mockedReportsRepository
                )
            }
            install(ContentNegotiation) { json() }
        }
    }

    fun mockVerifyUser(userId: String?, userToken: String?, successful: Boolean) {
        val verifyResult = slot<suspend () -> Unit>()
        coEvery {
            mockedUserManager.verifyUser(
                call = any(),
                logger = any(),
                userId = userId,
                userToken = userToken,
                execute = capture(verifyResult)
            )
        } coAnswers {
            if (successful) verifyResult.captured()
        }
    }

    fun verifyVerifyUser(userId: String?, userToken: String?) {
        coVerify {
            mockedUserManager.verifyUser(
                call = any(),
                logger = any(),
                userId = userId,
                userToken = userToken,
                execute = any()
            )
        }
    }

}
