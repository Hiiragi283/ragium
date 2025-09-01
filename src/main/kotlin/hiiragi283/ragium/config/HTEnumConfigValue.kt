package hiiragi283.ragium.config

import net.neoforged.neoforge.common.ModConfigSpec
import java.util.function.Supplier

/**
 * @see [mekanism.common.config.value.CachedBooleanValue]
 */
class HTEnumConfigValue<T : Enum<T>>(value: ModConfigSpec.ConfigValue<T>) :
    HTConfigValue<T>(value),
    Supplier<T> {
    override fun get(): T = value.get()
}
