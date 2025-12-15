package hiiragi283.ragium.api.math

import org.apache.commons.lang3.math.Fraction
import kotlin.math.min

fun fraction(value: Double): Fraction = Fraction.getFraction(value)

fun Double.toFraction(): Fraction = fraction(this)

operator fun Fraction.plus(other: Fraction): Fraction = this.add(other)

operator fun Fraction.minus(other: Fraction): Fraction = this.subtract(other)

operator fun Fraction.times(other: Fraction): Fraction = this.multiplyBy(other)

operator fun Fraction.div(other: Fraction): Fraction = this.divideBy(other)

// Int
fun fraction(numerator: Int, denominator: Int): Fraction = Fraction.getFraction(numerator, denominator)

fun fixedFraction(amount: Int, capacity: Int): Fraction {
    if (capacity <= 0) return Fraction.ZERO
    val fixedAmount: Int = min(amount, capacity)
    return fraction(fixedAmount, capacity)
}

fun fraction(numerator: Int): Fraction = fraction(numerator, 1)

fun Int.toFraction(denominator: Int): Fraction = fraction(this, denominator)

operator fun Fraction.plus(other: Int): Fraction = this.add(other.toFraction(1))

operator fun Fraction.minus(other: Int): Fraction = this.subtract(other.toFraction(1))

operator fun Fraction.times(other: Int): Fraction = this.multiplyBy(other.toFraction(1))

operator fun Fraction.div(other: Int): Fraction = this.divideBy(other.toFraction(1))

operator fun Int.plus(other: Fraction): Fraction = this.toFraction(1).add(other)

operator fun Int.minus(other: Fraction): Fraction = this.toFraction(1).subtract(other)

operator fun Int.times(other: Fraction): Fraction = this.toFraction(1).multiplyBy(other)

operator fun Int.div(other: Fraction): Fraction = this.toFraction(1).divideBy(other)

// Long
fun fixedFraction(amount: Long, capacity: Long): Fraction {
    if (capacity <= 0) return Fraction.ZERO
    val fixedAmount: Long = min(amount, capacity)
    return fraction(fixedAmount / capacity.toDouble())
}

// Float
fun fraction(value: Float): Fraction = fraction(value.toDouble())

fun Float.toFraction(): Fraction = fraction(this)

operator fun Fraction.plus(other: Float): Fraction = this.add(other.toFraction())

operator fun Fraction.minus(other: Float): Fraction = this.subtract(other.toFraction())

operator fun Fraction.times(other: Float): Fraction = this.multiplyBy(other.toFraction())

operator fun Fraction.div(other: Float): Fraction = this.divideBy(other.toFraction())

operator fun Float.plus(other: Fraction): Fraction = this.toFraction().add(other)

operator fun Float.minus(other: Fraction): Fraction = this.toFraction().subtract(other)

operator fun Float.times(other: Fraction): Fraction = this.toFraction().multiplyBy(other)

operator fun Float.div(other: Fraction): Fraction = this.toFraction().divideBy(other)
