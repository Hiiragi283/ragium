package hiiragi283.ragium.transfer.holder

import hiiragi283.lib.transfer.holder.HTCapabilityHolder
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
