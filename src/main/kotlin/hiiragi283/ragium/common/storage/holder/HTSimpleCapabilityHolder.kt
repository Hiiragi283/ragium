package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.storage.HTTransferIO
import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import net.minecraft.core.Direction

abstract class HTSimpleCapabilityHolder(private val transferProvider: HTTransferIO.Provider?) : HTCapabilityHolder {
    final override fun canInsert(side: Direction?): Boolean = when (side) {
        null -> false
        else -> transferProvider?.apply(side)?.canInsert ?: true
    }

    final override fun canExtract(side: Direction?): Boolean = when (side) {
        null -> false
        else -> transferProvider?.apply(side)?.canExtract ?: true
    }
}
