package hiiragi283.ragium.api.util.access

import net.minecraft.core.Direction

fun interface HTAccessConfigSetter {
    fun setAccessConfig(side: Direction, value: HTAccessConfiguration)
}
