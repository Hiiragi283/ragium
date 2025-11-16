package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import net.minecraft.core.Direction
import kotlin.collections.plusAssign

/**
 * @see mekanism.common.capabilities.holder.ConfigHolder
 */
abstract class HTSlottedCapabilityHolder<SLOT : Any>(
    configGetter: HTAccessConfigGetter?,
    private val slots: List<SLOT>,
    private val inputSlots: List<SLOT>,
    private val outputSlots: List<SLOT>,
) : HTConfigCapabilityHolder(configGetter) {
    protected fun getSlots(side: Direction?): List<SLOT> = when {
        side == null || this.configGetter == null -> slots
        else -> configGetter.getAccessConfig(side).let(::getSlots)
    }

    private fun getSlots(config: HTAccessConfig): List<SLOT> = when (config) {
        HTAccessConfig.INPUT_ONLY -> inputSlots
        HTAccessConfig.OUTPUT_ONLY -> outputSlots
        HTAccessConfig.BOTH -> buildList {
            addAll(inputSlots)
            addAll(outputSlots)
        }
        HTAccessConfig.DISABLED -> listOf()
    }

    abstract class Builder<SLOT : Any, HOLDER : HTCapabilityHolder>(
        protected val configGetter: HTAccessConfigGetter?,
        private val factory: (HTAccessConfigGetter?, List<SLOT>, List<SLOT>, List<SLOT>) -> HOLDER,
    ) {
        private var hasBuilt = false
        private val slots: MutableList<SLOT> = mutableListOf()
        private val inputSlots: MutableList<SLOT> = mutableListOf()
        private val outputSlots: MutableList<SLOT> = mutableListOf()

        fun <T : SLOT> addSlot(config: HTSlotInfo, slot: T): T {
            check(!hasBuilt) { "Builder has already built" }
            slots += slot
            if (config == HTSlotInfo.INPUT || config == HTSlotInfo.BOTH) {
                inputSlots += slot
            }
            if (config == HTSlotInfo.OUTPUT || config == HTSlotInfo.BOTH) {
                outputSlots += slot
            }
            RagiumAPI.LOGGER.debug("Added slot {} for config {}", slot, config)
            return slot
        }

        fun build(): HOLDER? {
            hasBuilt = true
            return when {
                slots.isEmpty() -> null
                else -> factory(configGetter, slots, inputSlots, outputSlots)
            }
        }
    }
}
