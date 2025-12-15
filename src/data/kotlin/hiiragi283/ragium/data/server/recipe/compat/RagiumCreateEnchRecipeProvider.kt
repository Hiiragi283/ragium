package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.setup.RagiumBlocks
import plus.dragons.createenchantmentindustry.common.kinetics.grindstone.GrindingRecipe
import plus.dragons.createenchantmentindustry.common.registry.CEIFluids

object RagiumCreateEnchRecipeProvider : HTRecipeProvider.Integration(RagiumConst.CREATE_ENCH) {
    override fun buildRecipeInternal() {
        grinding()
    }

    @JvmStatic
    private fun grinding() {
        // Exp Berries -> Exp
        GrindingRecipe
            .builder(RagiumBlocks.EXP_BERRIES.id)
            .require(RagiumBlocks.EXP_BERRIES)
            .output(CEIFluids.EXPERIENCE.get(), 50)
            .build(output)
    }
}
