package com.skintker.data.components

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
        return if (continuousData.size == otherContinuousData.size) {
            corrCalc.correlation(
                continuousData.map { it.toDouble() }.toDoubleArray(),
                otherContinuousData.map { it.toDouble() }.toDoubleArray()
            )
        } else Double.NaN
    }

    fun calculateCorrelationBinary(continuousData: List<Int>, binaryData: List<Boolean>): Double {
        return if (continuousData.size == binaryData.size) {
            corrCalc.correlation(
                continuousData.map { it.toDouble() }.toDoubleArray(),
                binaryData.map { if (it) 1.0 else 0.0 }.toDoubleArray()
            )
        } else Double.NaN
    }

    fun isAnyGroupSignificant(groups: Map<Int, DoubleArray>): Boolean {
        //TODO: Write logs if any exception is raised.
        return try {
            anovaCalc.anovaPValue(groups.values) <= 0.05
        } catch (e: NullArgumentException) {
            false
        } catch (e: DimensionMismatchException) {
            false
        } catch (e: ConvergenceException) {
            false
        } catch (e: MaxCountExceededException) {
            false
        }
    }


}
