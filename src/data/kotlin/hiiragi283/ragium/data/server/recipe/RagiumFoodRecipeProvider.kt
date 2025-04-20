package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumFoodRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Chocolate
        HTMachineRecipeBuilder(RagiumRecipes.INFUSING)
            .itemOutput(RagiumItems.Ingots.CHOCOLATE)
            .itemInput(Tags.Items.CROPS_COCOA_BEAN)
            .milkInput(250)
            .save(output)
        // Melon Pie
        HTShapelessRecipeBuilder(RagiumItems.MELON_PIE)
            .addIngredient(Tags.Items.CROPS_MELON)
            .addIngredient(Items.SUGAR)
            .addIngredient(Tags.Items.EGGS)
            .save(output)

        // Ambrosia
        HTMachineRecipeBuilder(RagiumRecipes.INFUSING)
            .itemOutput(RagiumItems.AMBROSIA)
            .itemInput(HTTagPrefixes.STORAGE_BLOCK, CommonMaterials.CHOCOLATE, 64)
            .fluidInput(Tags.Fluids.HONEY, 1000 * 64)
            .save(output)

        // Fever Cherry
        HTShapedRecipeBuilder(RagiumItems.FEVER_CHERRY)
            .hollow8()
            .define('A', Tags.Items.STORAGE_BLOCKS_GOLD)
            .define('B', RagiumItems.RAGI_CHERRY)
            .save(output)

        meat(output)
        sponge(output)
    }

    private fun meat(output: RecipeOutput) {
        // Minced Meat
        HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .itemInput(Tags.Items.FOODS_RAW_MEAT)
            .saveSuffixed(output, "_from_meat")
        HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .itemInput(Tags.Items.FOODS_RAW_FISH)
            .saveSuffixed(output, "_from_fish")
        HTMachineRecipeBuilder(RagiumRecipes.CRUSHING)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .itemInput(Items.ROTTEN_FLESH)
            .savePrefixed(output, "rotten_")
        // Meat Ingot
        HTShapedRecipeBuilder(RagiumItems.MEAT_INGOT, 3)
            .pattern("AAA")
            .define('A', RagiumItems.MINCED_MEAT)
            .save(output)

        HTCookingRecipeBuilder
            .smoking(RagiumItems.COOKED_MEAT_INGOT)
            .addIngredient(RagiumItems.MEAT_INGOT)
            .setExp(0.35f)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.CANNED_COOKED_MEAT, 8)
            .hollow8()
            .define('A', RagiumItems.COOKED_MEAT_INGOT)
            .define('B', HTTagPrefixes.INGOT, VanillaMaterials.IRON)
            .save(output)
    }

    private fun sponge(output: RecipeOutput) {
        // Sponge
        HTShapedRecipeBuilder(RagiumBlocks.SPONGE_CAKE, 4)
            .cross8()
            .define('A', Tags.Items.CROPS_WHEAT)
            .define('B', Items.SUGAR)
            .define('C', Tags.Items.EGGS)
            .save(output)

        addSlab(output, RagiumBlocks.SPONGE_CAKE, RagiumBlocks.SPONGE_CAKE_SLAB)
        // Cakes
        HTShapedRecipeBuilder(Items.CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', Tags.Items.BUCKETS_MILK)
            .define('B', Items.SUGAR)
            .define('C', Tags.Items.EGGS)
            .define('D', RagiumBlocks.SPONGE_CAKE_SLAB)
            .saveSuffixed(output, "_with_sponge")

        HTShapedRecipeBuilder(RagiumBlocks.SWEET_BERRIES_CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', RagiumItemTags.FOODS_CHOCOLATE)
            .define('B', Tags.Items.FOODS_BERRY)
            .define('C', Tags.Items.EGGS)
            .define('D', RagiumBlocks.SPONGE_CAKE_SLAB)
            .saveSuffixed(output, "_with_sponge")

        HTShapelessRecipeBuilder(RagiumItems.SWEET_BERRIES_CAKE_PIECE, 8)
            .addIngredient(RagiumBlocks.SWEET_BERRIES_CAKE)
            .save(output)

        HTShapedRecipeBuilder(RagiumBlocks.SWEET_BERRIES_CAKE)
            .hollow()
            .define('A', RagiumItems.SWEET_BERRIES_CAKE_PIECE)
            .save(output)
    }
}
