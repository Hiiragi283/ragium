package hiiragi283.lib.config

import hiiragi283.lib.text.HTHasTranslationKey
import net.neoforged.neoforge.common.ModConfigSpec

fun ModConfigSpec.Builder.translation(hasKey: HTHasTranslationKey): ModConfigSpec.Builder = this.translation(hasKey.translationKey)

fun ModConfigSpec.Builder.definePositiveInt(path: String, defaultValue: Int, min: Int = 1): ModConfigSpec.IntValue = defineInRange(path, defaultValue, min, Int.MAX_VALUE)

fun ModConfigSpec.Builder.definePositiveDouble(
    path: String,
    defaultValue: Double,
    min: Number,
    max: Number,
): ModConfigSpec.DoubleValue = defineInRange(path, defaultValue, min.toDouble(), max.toDouble())
