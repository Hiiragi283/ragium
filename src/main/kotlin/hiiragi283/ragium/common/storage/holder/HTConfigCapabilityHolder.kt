package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import net.minecraft.core.Direction

abstract class HTConfigCapabilityHolder(protected val configGetter: HTAccessConfigGetter?) : HTCapabilityHolder {
    final override fun canInsert(side: Direction?): Boolean = when (side) {
        null -> false
        else -> configGetter?.getAccessConfig(side)?.canInsert ?: true
    }

    final override fun canExtract(side: Direction?): Boolean = when (side) {
        null -> false
        else -> configGetter?.getAccessConfig(side)?.canExtract ?: true
    }
}
