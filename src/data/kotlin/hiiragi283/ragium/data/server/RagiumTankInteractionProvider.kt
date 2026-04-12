package hiiragi283.ragium.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tank.HTTankInteractionProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.core.common.data.tank.HTSimpleTankInteraction
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items

class RagiumTankInteractionProvider(context: HTDataGenContext) : HTTankInteractionProvider(context, RagiumAPI.MOD_ID) {
    override fun gather() {
        // Mercury
        tankInteraction(Items.GLASS_BOTTLE.toLike(), RagiumItems.MERCURY_BOTTLE, RagiumFluids.MERCURY)
    }

    private fun tankInteraction(
        empty: HTSimpleItemHolderLike,
        filled: HTSimpleItemHolderLike,
        content: HTFluidContent,
        amount: Int = 250,
    ) {
        unconditional(
            filled.getId(),
            HTSimpleTankInteraction(
                empty,
                filled,
                content,
                amount,
                content.fluidTag.wrapOptional(),
            ),
        )
    }
}
