package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe

object RagiumToolRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addRecipes(output, holderLookup)
        // Tool
        RagiumItems.RAGI_ALLOY_TOOLS.addRecipes(output, holderLookup)
        RagiumItems.AZURE_STEEL_TOOLS.addRecipes(output, holderLookup)

        HTShapelessRecipeBuilder(RagiumItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(HTTagPrefix.GEM, VanillaMaterials.EMERALD)
            .save(output)
        // Mold
        HTShapedRecipeBuilder(RagiumItems.Molds.BLANK)
            .pattern(
                "AA",
                "AA",
                "B ",
            ).define('A', HTTagPrefix.INGOT, RagiumMaterials.AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)

        for (mold: RagiumItems.Molds in RagiumItems.Molds.entries) {
            output.accept(
                RagiumAPI.id("stonecutting/${mold.holder.id.path}"),
                StonecutterRecipe(
                    "mold",
                    Ingredient.of(RagiumItemTags.MOLDS_BLANK),
                    mold.toStack(),
                ),
                null,
            )
        }
    }
}
