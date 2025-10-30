package hiiragi283.ragium.api.function

import net.minecraft.util.Mth

fun Int.clamp(range: IntRange): Int = Mth.clamp(this, range.first, range.last)

fun Long.clamp(range: LongRange): Long = Mth.clamp(this, range.first, range.last)
