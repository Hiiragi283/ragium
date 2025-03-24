package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilder
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluids

object RagiumFluidRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        centrifuging(output, holderLookup)
        infusing(output, holderLookup)
    }

    //    Centrifuging    //

    private fun centrifuging(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Magma Block -> Cobblestone + Lava
        HTMachineRecipeBuilder(RagiumRecipes.CENTRIFUGING)
            .itemOutput(Items.COBBLESTONE)
            .fluidOutput(Fluids.LAVA, 100)
            .itemInput(Items.MAGMA_BLOCK)
            .saveSuffixed(output, "_from_magma_block")
    }

    //    Infusing    //

    private fun infusing(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Dirt -> Mud
        HTMachineRecipeBuilder(RagiumRecipes.INFUSING)
            .itemOutput(Items.MUD)
            .itemInput(Items.DIRT)
            .waterInput()
            .saveSuffixed(output, "_from_dirt")
        // Silt -> Clay
        HTMachineRecipeBuilder(RagiumRecipes.INFUSING)
            .itemOutput(Items.CLAY)
            .itemInput(RagiumBlocks.SILT)
            .waterInput()
            .saveSuffixed(output, "_from_silt")
    }
}
