package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput

object RagiumToolRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        RagiumItems.AZURE_STEEL_ARMORS.addRecipes(output, holderLookup)

        RagiumItems.RAGI_ALLOY_TOOLS.addRecipes(output, holderLookup)
        RagiumItems.AZURE_STEEL_TOOLS.addRecipes(output, holderLookup)
    }
}
