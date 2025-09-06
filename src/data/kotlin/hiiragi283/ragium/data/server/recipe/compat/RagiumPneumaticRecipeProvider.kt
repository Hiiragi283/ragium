package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import me.desht.pneumaticcraft.datagen.recipe.FluidMixerRecipeBuilder
import net.neoforged.neoforge.fluids.FluidStack

object RagiumPneumaticRecipeProvider : HTRecipeProvider.Integration(RagiumConst.PNEUMATIC) {
    override fun buildRecipeInternal() {
        FluidMixerRecipeBuilder(
            RagiumFluidContents.CRIMSON_BLOOD.toIngredient(1000),
            RagiumFluidContents.DEW_OF_THE_WARP.toIngredient(1000),
            FluidStack.EMPTY,
            RagiumItems.getGem(RagiumMaterialType.ELDRITCH_PEARL).toStack(),
            2f,
            15,
        ).save(output)
    }
}
