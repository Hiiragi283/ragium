package hiiragi283.ragium.data.server

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.tank.HTTankInteractionProvider
import hiiragi283.ragium.common.data.tank.HTPotionTankInteraction
import hiiragi283.ragium.common.data.tank.HTSimpleTankInteraction
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items

class RagiumTankInteractionProvider(context: HTDataGenContext) : HTTankInteractionProvider(context, RagiumAPI.MOD_ID) {
    override fun gather() {
        val glassBottle: HTSimpleItemHolderLike = Items.GLASS_BOTTLE.toLike()
        // Experience
        tankInteraction(glassBottle, Items.EXPERIENCE_BOTTLE.toLike(), HCFluids.EXPERIENCE)
        // Honey Bottle
        tankInteraction(glassBottle, Items.HONEY_BOTTLE.toLike(), HCFluids.HONEY)
        // Mushroom Stew
        tankInteraction(Items.BOWL.toLike(), Items.MUSHROOM_STEW.toLike(), HCFluids.MUSHROOM_STEW)
        // Dragon Breath
        tankInteraction(glassBottle, Items.DRAGON_BREATH.toLike(), HCFluids.DRAGON_BREATH)
        // Potion Bottle
        unconditional(HTConst.MINECRAFT.toId("potion"), HTPotionTankInteraction)
        // Mercury
        tankInteraction(glassBottle, RagiumItems.MERCURY_BOTTLE, RagiumFluids.MERCURY)

        // Sponge
        tankInteraction(Items.SPONGE.toLike(), Items.WET_SPONGE.toLike(), VanillaFluidContents.WATER, HTConst.DEFAULT_FLUID_AMOUNT)
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
