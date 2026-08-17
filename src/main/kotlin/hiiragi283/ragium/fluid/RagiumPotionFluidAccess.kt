package hiiragi283.ragium.fluid

import hiiragi283.lib.item.alchemy.HTPotionFluidAccess
import hiiragi283.lib.registry.HTFluidContent

class RagiumPotionFluidAccess : HTPotionFluidAccess {
    override val fluidContent: HTFluidContent get() = RagiumFluids.POTION
}
