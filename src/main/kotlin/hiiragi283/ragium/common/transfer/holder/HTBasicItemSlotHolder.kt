package hiiragi283.ragium.common.transfer.holder

import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTItemSlot

class HTBasicItemSlotHolder private constructor(
    configGetter: HTSlotInfoProvider?,
    slots: List<HTItemSlot>,
    slotMap: Map<HTSlotInfo, List<HTItemSlot>>,
) : HTSlottedCapabilityHolder<HTItemSlot>(configGetter, slots, slotMap),
    HTResourceSlotHolder<HTItemSlot> {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTSlotInfoProvider?): Builder = Builder(configGetter)
    }

    class Builder(configGetter: HTSlotInfoProvider?) : HTSlottedCapabilityHolder.Builder<HTItemSlot, HTResourceSlotHolder<HTItemSlot>>(configGetter, ::HTBasicItemSlotHolder)
}
