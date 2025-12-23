package hiiragi283.ragium.common.storge.holder

import net.minecraft.core.Direction

fun interface HTSlotInfoProvider {
    fun getSlotInfo(side: Direction): HTSlotInfo
}
