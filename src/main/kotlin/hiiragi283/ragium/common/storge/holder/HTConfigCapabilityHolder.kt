package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.storage.holder.HTCapabilityHolder
import net.minecraft.core.Direction

abstract class HTConfigCapabilityHolder(protected val configGetter: HTSlotInfoProvider?) : HTCapabilityHolder {
    final override fun canInsert(side: Direction?): Boolean = when (side) {
        null -> false
        else -> configGetter?.getSlotInfo(side)?.canInsert ?: true
    }

    final override fun canExtract(side: Direction?): Boolean = when (side) {
        null -> false
        else -> configGetter?.getSlotInfo(side)?.canExtract ?: true
    }
}
