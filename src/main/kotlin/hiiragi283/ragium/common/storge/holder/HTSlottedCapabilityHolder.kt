package hiiragi283.ragium.common.storge.holder

import com.mojang.logging.LogUtils
import hiiragi283.core.api.collection.ImmutableMultiMap
import hiiragi283.core.api.storage.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import org.slf4j.Logger
import kotlin.collections.plusAssign

/**
 * @see mekanism.common.capabilities.holder.ConfigHolder
 */
abstract class HTSlottedCapabilityHolder<SLOT : Any>(
    configGetter: HTSlotInfoProvider?,
    private val slots: List<SLOT>,
    private val slotMap: ImmutableMultiMap<HTSlotInfo, SLOT>,
) : HTConfigCapabilityHolder(configGetter) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    protected fun getSlots(side: Direction?): List<SLOT> = when {
        side == null || this.configGetter == null -> slots
        else -> configGetter.getSlotInfo(side).let(::getSlots)
    }

    private fun getSlots(info: HTSlotInfo): List<SLOT> = slotMap[info].toList()

    abstract class Builder<SLOT : Any, HOLDER : HTCapabilityHolder>(
        protected val configGetter: HTSlotInfoProvider?,
        private val factory: (HTSlotInfoProvider?, List<SLOT>, ImmutableMultiMap<HTSlotInfo, SLOT>) -> HOLDER,
    ) {
        private var hasBuilt = false
        private val slots: MutableList<SLOT> = mutableListOf()
        private val slotMap: ImmutableMultiMap.Builder<HTSlotInfo, SLOT> = ImmutableMultiMap.Builder()

        fun addSlot(info: HTSlotInfo, slot: SLOT) {
            check(!hasBuilt) { "Builder has already built" }
            slots += slot
            when (info) {
                HTSlotInfo.NONE -> return
                HTSlotInfo.BOTH -> {
                    slotMap.put(info, slot)
                    slotMap.put(HTSlotInfo.INPUT, slot)
                    slotMap.put(HTSlotInfo.OUTPUT, slot)
                }
                else -> {
                    slotMap.put(info, slot)
                    slotMap.put(HTSlotInfo.BOTH, slot)
                }
            }
            LOGGER.debug("Added slot {} for config {}", slot, info)
        }

        fun build(): HOLDER? {
            hasBuilt = true
            return when {
                slots.isEmpty() -> null
                else -> factory(configGetter, slots, slotMap.build())
            }
        }
    }
}
