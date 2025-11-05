package hiiragi283.ragium.api.block.attribute

import java.util.function.IntSupplier
import kotlin.math.max

/**
 * @see mekanism.common.block.attribute.AttributeEnergy
 */
@JvmRecord
data class HTEnergyBlockAttribute(private val usage: IntSupplier, private val capacity: IntSupplier) : HTBlockAttribute {
    fun getRawCapacity(): Int = capacity.asInt

    fun getUsage(): Int = usage.asInt

    fun getCapacity(): Int = max(getRawCapacity(), getUsage())
}
