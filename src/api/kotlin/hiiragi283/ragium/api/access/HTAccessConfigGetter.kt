package hiiragi283.ragium.api.access

import net.minecraft.core.Direction

fun interface HTAccessConfigGetter {
    fun getAccessConfig(side: Direction): HTAccessConfig
}
