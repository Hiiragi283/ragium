package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.text.Text
import net.minecraft.core.Direction

fun interface HTSlotInfoProvider {
    fun getSlotInfo(side: Direction): HTSlotInfo

    fun getSlotInfoText(side: Direction): Text = getSlotInfo(side).getText(side)
}
