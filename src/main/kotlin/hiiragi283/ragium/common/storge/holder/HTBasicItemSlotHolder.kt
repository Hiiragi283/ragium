package hiiragi283.ragium.common.storge.holder

import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.access.HTAccessConfigGetter
import net.minecraft.core.Direction

class HTBasicItemSlotHolder private constructor(
    configGetter: HTAccessConfigGetter?,
    slots: List<HTItemSlot>,
    inputSlots: List<HTItemSlot>,
    outputSlots: List<HTItemSlot>,
) : HTSlottedCapabilityHolder<HTItemSlot>(configGetter, slots, inputSlots, outputSlots),
    HTItemSlotHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTAccessConfigGetter?): Builder = Builder(configGetter)
    }

    override fun getItemSlot(side: Direction?): List<HTItemSlot> = getSlots(side)

    class Builder(configGetter: HTAccessConfigGetter?) :
        HTSlottedCapabilityHolder.Builder<HTItemSlot, HTItemSlotHolder>(configGetter, ::HTBasicItemSlotHolder)
}
