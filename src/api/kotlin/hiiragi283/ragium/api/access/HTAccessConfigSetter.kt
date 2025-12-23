package hiiragi283.ragium.api.access

import net.minecraft.core.Direction

fun interface HTAccessConfigSetter {
    fun setAccessConfig(side: Direction, value: HTAccessConfig)
}
