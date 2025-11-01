package hiiragi283.ragium.api.block.attribute

import java.util.function.IntSupplier
import kotlin.math.max

/**
 * @see mekanism.common.block.attribute.AttributeEnergy
 */
@JvmRecord
data class HTEnergyBlockAttribute(private val capacity: IntSupplier, private val usage: IntSupplier = IntSupplier { capacity.asInt * 20 }) :
    HTBlockAttribute {
    fun getRawCapacity(): Int = capacity.asInt

    fun getUsage(): Int = usage.asInt

    fun getCapacity(): Int = max(getRawCapacity(), getUsage())
}
