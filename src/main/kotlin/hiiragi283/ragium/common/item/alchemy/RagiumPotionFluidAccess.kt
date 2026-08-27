package hiiragi283.ragium.common.item.alchemy

import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionFluidAccess
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.common.fluid.RagiumFluids
import net.minecraft.core.component.DataComponentType

class RagiumPotionFluidAccess : HTPotionFluidAccess {
    override val fluidContent: HTFluidContent get() = RagiumFluids.POTION
    override val bottleType: DataComponentType<HTBottleType> get() = RagiumDataComponents.BOTTLE_TYPE
}
