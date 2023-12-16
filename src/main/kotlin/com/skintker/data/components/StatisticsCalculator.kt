package com.skintker.data.components

import com.skintker.data.Constants.P_VALUE
import org.apache.commons.math3.exception.ConvergenceException
import org.apache.commons.math3.exception.DimensionMismatchException
import org.apache.commons.math3.exception.MaxCountExceededException
import org.apache.commons.math3.exception.NullArgumentException
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import org.apache.commons.math3.stat.inference.OneWayAnova

class StatisticsCalculator(private val corrCalc: PearsonsCorrelation, private val anovaCalc: OneWayAnova) {

    fun getCorrelations(
        continuousData: List<Int>,
        binaryDataMap: Map<String, List<Boolean>>
    ): Map<String, Double> {
        return if (binaryDataMap.all { it.value.size == continuousData.size }) {
            binaryDataMap.map { (dataName, binaryData) ->
                dataName to calculateCorrelationBinary(continuousData, binaryData)
            }.toMap()
        } else emptyMap()
    }

    fun calculateCorrelation(continuousData: List<Int>, otherContinuousData: List<Int>): Double {
        return if (continuousData.size == otherContinuousData.size && continuousData.size >= 2) {
            calculateCorrelationSafe(
                continuousData.map { it.toDouble() }.toDoubleArray(),
                otherContinuousData.map { it.toDouble() }.toDoubleArray()
            )
        } else Double.NaN
    }

    fun calculateCorrelationBinary(continuousData: List<Int>, binaryData: List<Boolean>): Double {
        return if (continuousData.size == binaryData.size && continuousData.size >= 2) {
            calculateCorrelationSafe(
                continuousData.map { it.toDouble() }.toDoubleArray(),
                binaryData.map { if (it) 1.0 else 0.0 }.toDoubleArray()
            )
        } else Double.NaN
    }

    private fun calculateCorrelationSafe(xList: DoubleArray, yList: DoubleArray) =
        runCatching { corrCalc.correlation(xList, yList) }.fold(
            onSuccess = { it },
            onFailure = {
                println("Exception during correlation: ${it.message}")
                Double.NaN
            }
        )

    fun isAnyGroupSignificant(groups: Map<Int, DoubleArray>) =
        runCatching { anovaCalc.anovaPValue(groups.values) }.fold(
            onSuccess = { it <= P_VALUE },
            onFailure = {
                println("Exception during ANOVA: ${it.message}")
                false
            }
        )

}
