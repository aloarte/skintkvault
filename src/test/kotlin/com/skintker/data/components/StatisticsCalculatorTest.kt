package com.skintker.data.components

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.commons.math3.exception.DimensionMismatchException
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import org.apache.commons.math3.stat.inference.OneWayAnova
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StatisticsCalculatorTest {

    private val corrCalc = mockk<PearsonsCorrelation>()

    private val anovaCalc = mockk<OneWayAnova>()

    private lateinit var calculator: StatisticsCalculator

    @Before
    fun setup() {
        calculator = StatisticsCalculator(corrCalc, anovaCalc)
    }

    @Test
    fun `test get biserial correlations`() {
        val continuousData = listOf(1, 2, 3, 4, 5, 1, 2, 3, 4, 5)
        val continuousDataD = listOf(1.0, 2.0, 3.0, 4.0, 5.0, 1.0, 2.0, 3.0, 4.0, 5.0).toDoubleArray()
        val binaryMap = mapOf(
            "data 1" to listOf(true, false, false, true, false, true, false, false, true, false),
            "data 2" to listOf(false, false, true, true, true, true, false, false, false, true)
        )
        val binaryMapD = mapOf(
            "data 1" to listOf(1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0).toDoubleArray(),
            "data 2" to listOf(0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0).toDoubleArray()
        )
        every { corrCalc.correlation(continuousDataD, binaryMapD["data 1"]) } returns 0.5
        every { corrCalc.correlation(continuousDataD, binaryMapD["data 2"]) } returns -0.5

        val correlations = calculator.getCorrelations(continuousData, binaryMap)

        verify { corrCalc.correlation(continuousDataD, binaryMapD["data 1"]) }
        verify { corrCalc.correlation(continuousDataD, binaryMapD["data 2"]) }
        Assert.assertEquals(2, correlations.size)
        Assert.assertEquals(0.5, correlations["data 1"])
        Assert.assertEquals(-0.5, correlations["data 2"])
    }

    @Test
    fun `test get biserial correlations bad sizes`() {
        val continuousData = listOf(1, 2)
        val binaryMap = mapOf(
            "data 1" to listOf(true, false),
            "data 2" to listOf(false, false, true)
        )

        val correlations = calculator.getCorrelations(continuousData, binaryMap)

        verify(exactly = 0) { corrCalc.correlation(any(), any()) }
        Assert.assertEquals(0, correlations.size)
    }

    @Test
    fun `test calculate correlation `() {
        val continuousData = listOf(1, 2)
        val continuousDataD = listOf(1.0, 2.0)
        val otherContinuousData = listOf(4, 2)
        val otherContinuousDataD = listOf(4.0, 2.0)
        every {
            corrCalc.correlation(
                continuousDataD.toDoubleArray(),
                otherContinuousDataD.toDoubleArray()
            )
        } returns 0.5

        val correlation = calculator.calculateCorrelation(continuousData, otherContinuousData)

        verify { corrCalc.correlation(continuousDataD.toDoubleArray(), otherContinuousDataD.toDoubleArray()) }
        Assert.assertEquals(0.5, correlation, 0.0)
    }

    @Test
    fun `test calculate correlation bad sizes`() {
        val continuousData = listOf(1, 2)
        val otherContinuousData = listOf(4, 2, 0)

        val correlation = calculator.calculateCorrelation(continuousData, otherContinuousData)

        verify(exactly = 0) { corrCalc.correlation(any(), any()) }
        Assert.assertEquals(Double.NaN, correlation, 0.0)
    }

    @Test
    fun `test calculate correlation only 1 dimension`() {
        val continuousData = listOf(1)
        val otherContinuousData = listOf(4)

        val correlation = calculator.calculateCorrelation(continuousData, otherContinuousData)

        verify(exactly = 0) { corrCalc.correlation(any(), any()) }
        Assert.assertEquals(Double.NaN, correlation, 0.0)
    }

    @Test
    fun `test calculate binary correlation `() {
        val continuousData = listOf(1, 2)
        val continuousDataD = listOf(1.0, 2.0)
        val otherContinuousData = listOf(true, false)
        val otherContinuousDataD = listOf(1.0, 0.0)
        every {
            corrCalc.correlation(
                continuousDataD.toDoubleArray(),
                otherContinuousDataD.toDoubleArray()
            )
        } returns 0.5

        val correlation = calculator.calculateCorrelationBinary(continuousData, otherContinuousData)

        verify { corrCalc.correlation(continuousDataD.toDoubleArray(), otherContinuousDataD.toDoubleArray()) }
        Assert.assertEquals(0.5, correlation, 0.0)
    }

    @Test
    fun `test calculate binary correlation bad sizes`() {
        val continuousData = listOf(1, 2)
        val otherContinuousData = listOf(true, false, false)

        val correlation = calculator.calculateCorrelationBinary(continuousData, otherContinuousData)

        verify(exactly = 0) { corrCalc.correlation(any(), any()) }
        Assert.assertEquals(Double.NaN, correlation, 0.0)
    }

    @Test
    fun `test calculate binary correlation only 1 dimension`() {
        val continuousData = listOf(1)
        val otherContinuousData = listOf(true)

        val correlation = calculator.calculateCorrelationBinary(continuousData, otherContinuousData)

        verify(exactly = 0) { corrCalc.correlation(any(), any()) }
        Assert.assertEquals(Double.NaN, correlation, 0.0)
    }

    @Test
    fun `test is any group significant true`() {
        val binaryMap = mapOf(
            1 to listOf(1.0, 2.0).toDoubleArray(),
            2 to listOf(1.0, 4.0).toDoubleArray()
        )
        every { anovaCalc.anovaPValue(binaryMap.values) } returns 0.04

        Assert.assertTrue(calculator.isAnyGroupSignificant(binaryMap))

        verify { anovaCalc.anovaPValue(binaryMap.values) }
    }

    @Test
    fun `test is any group significant false`() {
        val binaryMap = mapOf(
            1 to listOf(1.0, 2.0).toDoubleArray(),
            2 to listOf(1.0, 4.0).toDoubleArray()
        )
        every { anovaCalc.anovaPValue(binaryMap.values) } returns 0.20

        Assert.assertFalse(calculator.isAnyGroupSignificant(binaryMap))

        verify { anovaCalc.anovaPValue(binaryMap.values) }
    }

    @Test
    fun `test is any group significant exception raised false`() {
        val binaryMap = mapOf(
            1 to listOf(1.0, 2.0).toDoubleArray(),
            2 to listOf(1.0, 4.0).toDoubleArray()
        )
        every { anovaCalc.anovaPValue(binaryMap.values) } throws DimensionMismatchException(1, 2)

        Assert.assertFalse(calculator.isAnyGroupSignificant(binaryMap))

        verify { anovaCalc.anovaPValue(binaryMap.values) }
    }
}
