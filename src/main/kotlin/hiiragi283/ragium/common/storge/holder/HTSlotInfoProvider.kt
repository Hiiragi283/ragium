package hiiragi283.ragium.common.storge.holder

import net.minecraft.core.Direction
import net.minecraft.network.chat.Component

fun interface HTSlotInfoProvider {
    fun getSlotInfo(side: Direction): HTSlotInfo

    fun getSlotInfoText(side: Direction): Component = getSlotInfo(side).getText(side)
}
