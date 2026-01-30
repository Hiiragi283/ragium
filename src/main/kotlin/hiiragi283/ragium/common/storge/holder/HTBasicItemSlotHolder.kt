package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemSlot
import net.minecraft.core.Direction

class HTBasicItemSlotHolder private constructor(
    configGetter: HTSlotInfoProvider?,
    slots: List<HTItemSlot>,
    slotMap: Map<HTSlotInfo, List<HTItemSlot>>,
) : HTSlottedCapabilityHolder<HTItemSlot>(configGetter, slots, slotMap),
    HTItemSlotHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTSlotInfoProvider?): Builder = Builder(configGetter)
    }

    override fun getItemSlot(side: Direction?): List<HTItemSlot> = getSlots(side)

    class Builder(configGetter: HTSlotInfoProvider?) :
        HTSlottedCapabilityHolder.Builder<HTItemSlot, HTItemSlotHolder>(configGetter, ::HTBasicItemSlotHolder)
}
