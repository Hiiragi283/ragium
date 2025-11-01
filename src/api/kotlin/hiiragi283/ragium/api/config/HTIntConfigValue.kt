package hiiragi283.ragium.api.config

import net.neoforged.neoforge.common.ModConfigSpec
import java.util.function.IntSupplier
import java.util.function.LongSupplier

/**
 * @see mekanism.common.config.value.CachedIntValue
 */
class HTIntConfigValue(value: ModConfigSpec.IntValue) :
    HTConfigValue<Int>(value),
    IntSupplier,
    LongSupplier {
    override fun getAsInt(): Int = value.get()

    override fun getAsLong(): Long = asInt.toLong()
}
