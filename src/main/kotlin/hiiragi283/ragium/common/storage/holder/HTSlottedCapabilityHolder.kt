package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.collection.ImmutableMultiMap
import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
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
    private val map: ImmutableMultiMap<HTAccessConfig, SLOT>,
) : HTConfigCapabilityHolder(configGetter) {
    protected fun getSlots(side: Direction?): List<SLOT> = when {
        side == null || this.configGetter == null -> slots
        else -> configGetter.getAccessConfig(side).let(map::get).toList()
    }

    abstract class Builder<SLOT : Any, HOLDER : HTCapabilityHolder>(
        protected val configGetter: HTAccessConfigGetter?,
        private val factory: (HTAccessConfigGetter?, List<SLOT>, ImmutableMultiMap<HTAccessConfig, SLOT>) -> HOLDER,
    ) {
        private var hasBuilt = false
        private val slots: MutableList<SLOT> = mutableListOf()
        private val builder: ImmutableMultiMap.Builder<HTAccessConfig, SLOT> = ImmutableMultiMap.Builder()

        fun <T : SLOT> addSlot(config: HTAccessConfig, slot: T): T {
            check(!hasBuilt) { "Builder has already built" }
            slots += slot
            builder[config] = slot
            if (config != HTAccessConfig.NONE) {
                builder[HTAccessConfig.BOTH] = slot
            }
            return slot
        }

        fun build(): HOLDER {
            hasBuilt = true
            return factory(configGetter, slots, builder.build())
        }
    }
}
