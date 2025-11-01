package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.collection.ImmutableMultiMap
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.storage.holder.HTExperienceTankHolder
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import net.minecraft.core.Direction

class HTBasicExperienceTankHolder private constructor(
    configGetter: HTAccessConfigGetter?,
    slots: List<HTExperienceTank>,
    map: ImmutableMultiMap<HTAccessConfig, HTExperienceTank>,
) : HTSlottedCapabilityHolder<HTExperienceTank>(configGetter, slots, map),
    HTExperienceTankHolder {
    companion object {
        @JvmStatic
        fun builder(configGetter: HTAccessConfigGetter?): Builder = Builder(configGetter)
    }

    override fun getExperienceTank(side: Direction?): List<HTExperienceTank> = getSlots(side)

    class Builder(configGetter: HTAccessConfigGetter?) :
        HTSlottedCapabilityHolder.Builder<HTExperienceTank, HTExperienceTankHolder>(configGetter, ::HTBasicExperienceTankHolder)
}
