package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.config.HTDoubleConfigValue
import hiiragi283.ragium.api.config.HTIntConfigValue
import net.neoforged.neoforge.common.ModConfigSpec

fun ModConfigSpec.Builder.definePositiveInt(path: String, defaultValue: Int, min: Int = 1): HTIntConfigValue =
    defineInRange(path, defaultValue, min, Int.MAX_VALUE).let(::HTIntConfigValue)

fun ModConfigSpec.Builder.definePositiveDouble(
    path: String,
    defaultValue: Double,
    min: Double,
    max: Double,
): HTDoubleConfigValue = defineInRange(path, defaultValue, min, max).let(::HTDoubleConfigValue)
