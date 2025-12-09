package hiiragi283.ragium.api.config

import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.neoforged.neoforge.common.ModConfigSpec

fun ModConfigSpec.Builder.translation(hasKey: HTHasTranslationKey): ModConfigSpec.Builder = this.translation(hasKey.translationKey)

fun ModConfigSpec.Builder.definePositiveInt(path: String, defaultValue: Int, min: Int = 1): HTIntConfigValue =
    defineInRange(path, defaultValue, min, Int.MAX_VALUE).let(::HTIntConfigValue)

fun ModConfigSpec.Builder.definePositiveDouble(
    path: String,
    defaultValue: Double,
    min: Number,
    max: Number,
): HTDoubleConfigValue = defineInRange(path, defaultValue, min.toDouble(), max.toDouble()).let(::HTDoubleConfigValue)
