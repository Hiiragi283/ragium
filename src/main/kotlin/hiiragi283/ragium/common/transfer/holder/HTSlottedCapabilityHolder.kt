package hiiragi283.ragium.common.transfer.holder

import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.transfer.holder.HTCapabilityHolder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.Direction

/**
 * 参考 : [Mekanism - ConfigHolder](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/holder/ConfigHolder.java)
 */
abstract class HTSlottedCapabilityHolder<SLOT : Any>(
    configGetter: HTSlotInfoProvider?,
    private val slots: List<SLOT>,
    private val slotMap: Map<HTSlotInfo, List<SLOT>>
) : HTConfigCapabilityHolder(configGetter) {
    fun getSlots(side: Direction?): List<SLOT> = when {
        side == null || this.configGetter == null -> slots
        else -> configGetter.getSlotInfo(side).let(::getSlots)
    }

    private fun getSlots(info: HTSlotInfo): List<SLOT> = slotMap[info] ?: listOf()

    abstract class Builder<SLOT : Any, HOLDER : HTCapabilityHolder>(
        protected val configGetter: HTSlotInfoProvider?,
        private val factory: (HTSlotInfoProvider?, List<SLOT>, Map<HTSlotInfo, List<SLOT>>) -> HOLDER
    ) {
        private var hasBuilt = false
        private val slots: MutableList<SLOT> = ObjectArrayList()
        private val slotMap: MutableMap<HTSlotInfo, MutableList<SLOT>> = mutableEnumMapOf()

        private fun putSlot(info: HTSlotInfo, slot: SLOT) {
            slotMap.getOrPut(info, ::ObjectArrayList) += slot
        }

        fun <T : SLOT> addSlot(info: HTSlotInfo, slot: T): T {
            check(!hasBuilt) { "Builder has already built" }
            slots += slot
            when (info) {
                HTSlotInfo.NONE -> return slot

                HTSlotInfo.BOTH -> {
                    putSlot(info, slot)
                    putSlot(HTSlotInfo.INPUT, slot)
                    putSlot(HTSlotInfo.OUTPUT, slot)
                }

                else -> {
                    putSlot(info, slot)
                    putSlot(HTSlotInfo.BOTH, slot)
                }
            }
            // RagiumAPI.LOGGER.debug("Added slot {} for config {}", slot, info)
            return slot
        }

        fun build(): HOLDER? {
            hasBuilt = true
            return when {
                slots.isEmpty() -> null
                else -> factory(configGetter, slots, slotMap)
            }
        }
    }
}
