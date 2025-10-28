package hiiragi283.ragium.api.config

import net.neoforged.neoforge.common.ModConfigSpec

/**
 * @see mekanism.common.config.value.CachedValue
 */
abstract class HTConfigValue<T : Any>(protected val value: ModConfigSpec.ConfigValue<T>)
