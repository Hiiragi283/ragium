package hiiragi283.ragium.common.item.alchemy

import hiiragi283.lib.item.alchemy.HTPotionFluidAccess
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems

class RagiumPotionFluidAccess : HTPotionFluidAccess {
    override val fluidContent: HTFluidContent get() = RagiumFluids.POTION

    override val glassBottle: HTSimpleDeferredItem = HTSimpleDeferredItem(vanillaId("glass_bottle"))
    override val splashBottle: HTSimpleDeferredItem get() = RagiumItems.SPLASH_BOTTLE
    override val lingeringBottle: HTSimpleDeferredItem get() = RagiumItems.LINGERING_BOTTLE
}
