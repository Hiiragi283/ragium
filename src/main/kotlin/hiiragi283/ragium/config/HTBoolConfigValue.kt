package hiiragi283.ragium.config

import net.neoforged.neoforge.common.ModConfigSpec
import java.util.function.BooleanSupplier

/**
 * @see [mekanism.common.config.value.CachedBooleanValue]
 */
class HTBoolConfigValue(value: ModConfigSpec.BooleanValue) :
    HTConfigValue<Boolean>(value),
    BooleanSupplier {
    override fun getAsBoolean(): Boolean = value.get()
}
