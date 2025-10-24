package hiiragi283.ragium.api.util.access

import net.minecraft.core.Direction

fun interface HTAccessConfigGetter {
    fun getAccessConfig(side: Direction): HTAccessConfig
}
