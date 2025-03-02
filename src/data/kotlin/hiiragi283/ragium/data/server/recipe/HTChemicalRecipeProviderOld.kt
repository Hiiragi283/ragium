package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HTChemicalRecipeProviderOld : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerWither(output)
    }

    private fun registerWither(output: RecipeOutput) {
        // Wither Reagent
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.WITHER_ROSE)
            .itemOutput(RagiumItems.WITHER_REAGENT, 4)
            .saveSuffixed(output, "_from_rose")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.WITHER_SKELETON_SKULL)
            .itemOutput(RagiumItems.WITHER_REAGENT, 8)
            .saveSuffixed(output, "_from_skull")

        // Defoliant
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumItems.DEFOLIANT)
            .requires(Tags.Items.FERTILIZERS)
            .requires(RagiumItems.WITHER_REAGENT)
            .unlockedBy("has_reagent", has(RagiumItems.WITHER_REAGENT))
            .savePrefixed(output)
    }
}
