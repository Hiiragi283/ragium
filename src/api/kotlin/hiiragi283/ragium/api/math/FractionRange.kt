package hiiragi283.ragium.api.math

import org.apache.commons.lang3.math.Fraction

data class FractionRange(override val start: Fraction, override val endInclusive: Fraction) : ClosedFloatingPointRange<Fraction> {
    override fun lessThanOrEquals(a: Fraction, b: Fraction): Boolean = a <= b

    override fun contains(value: Fraction): Boolean = value in start..endInclusive

    override fun isEmpty(): Boolean = start > endInclusive

    override fun toString(): String = "$start..$endInclusive"
}
