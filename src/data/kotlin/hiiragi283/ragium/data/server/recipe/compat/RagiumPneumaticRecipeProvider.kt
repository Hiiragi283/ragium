package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import me.desht.pneumaticcraft.datagen.recipe.FluidMixerRecipeBuilder
import net.neoforged.neoforge.fluids.FluidStack

object RagiumPneumaticRecipeProvider : HTRecipeProvider.Integration(RagiumConst.PNEUMATIC) {
    override fun buildRecipeInternal() {
        FluidMixerRecipeBuilder(
            RagiumFluidContents.CRIMSON_SAP.toIngredient(1000),
            RagiumFluidContents.WARPED_SAP.toIngredient(1000),
            FluidStack.EMPTY,
            RagiumItems.Gems.ELDRITCH_PEARL.toStack(),
            2f,
            15,
        ).save(output)
    }
}
