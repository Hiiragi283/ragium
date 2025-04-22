package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.neoforged.neoforge.common.Tags

object RagiumToolRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addRecipes(output, holderLookup)
        // Tool
        RagiumItems.RAGI_ALLOY_TOOLS.addRecipes(output, holderLookup)
        RagiumItems.AZURE_STEEL_TOOLS.addRecipes(output, holderLookup)

        HTShapedRecipeBuilder(RagiumItems.ENDER_BUNDLE)
            .pattern(
                " A ",
                "ABA",
                "AAA",
            ).define('A', Tags.Items.LEATHERS)
            .define('B', Tags.Items.CHESTS_ENDER)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ITEM_MAGNET)
            .pattern(
                "A A",
                "B B",
                " C ",
            ).define('A', HTTagPrefixes.INGOT, RagiumMaterials.AZURE_STEEL)
            .define('B', HTTagPrefixes.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('C', HTTagPrefixes.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .save(output)

        HTShapelessRecipeBuilder(RagiumItems.TRADER_CATALOG)
            .addIngredient(Items.BOOK)
            .addIngredient(HTTagPrefixes.GEM, VanillaMaterials.EMERALD)
            .save(output)
        // Mold
        HTShapedRecipeBuilder(RagiumItems.Molds.BLANK)
            .pattern(
                "AA",
                "AA",
                "B ",
            ).define('A', HTTagPrefixes.INGOT, RagiumMaterials.AZURE_STEEL)
            .define('B', RagiumItemTags.TOOLS_FORGE_HAMMER)
            .save(output)

        for (mold: RagiumItems.Molds in RagiumItems.Molds.entries) {
            output.accept(
                RagiumAPI.id("stonecutting/${mold.path}"),
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
