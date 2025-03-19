package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.*
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumFoodRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        meat(output)
        milk(output)
        sponge(output)
    }

    private fun meat(output: RecipeOutput) {
        // Minced Meat
        HTSingleItemRecipeBuilder
            .crush(RagiumItems.MINCED_MEAT)
            .addIngredient(Tags.Items.FOODS_RAW_MEAT)
            .group("minced_meat")
            .saveSuffixed(output, "_from_meat")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.MINCED_MEAT)
            .addIngredient(Tags.Items.FOODS_RAW_FISH)
            .group("minced_meat")
            .saveSuffixed(output, "_from_fish")

        HTSingleItemRecipeBuilder
            .crush(RagiumItems.MINCED_MEAT)
            .addIngredient(Items.ROTTEN_FLESH)
            .group("minced_meat")
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
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.MEAT_SANDWICH)
            .pattern(
                "A",
                "B",
                "A",
            ).define('A', Tags.Items.FOODS_BREAD)
            .define('B', RagiumItems.COOKED_MEAT_INGOT)
            .save(output)
    }

    private fun milk(output: RecipeOutput) {
        // Butter
        HTCentrifugingRecipeBuilder(RagiumItems.BUTTER)
            .addIngredient(Tags.Items.BUCKETS_MILK)
            .addSubProduct(Items.BUCKET)
            .save(output)
        
        // Cheese
        
    }

    private fun sponge(output: RecipeOutput) {
        // Sponge
        HTShapedRecipeBuilder(RagiumBlocks.SPONGE_CAKE, 4)
            .cross8()
            .define('A', RagiumItemTags.FLOURS)
            .define('B', Items.SUGAR)
            .define('C', RagiumItems.BUTTER)
            .save(output)

        addSlab(output, RagiumBlocks.SPONGE_CAKE, RagiumBlocks.SPONGE_CAKE_SLAB)
        // Pies
        HTShapelessRecipeBuilder(Items.PUMPKIN_PIE, 2)
            .addIngredient(Tags.Items.CROPS_PUMPKIN)
            .addIngredient(Items.SUGAR)
            .addIngredient(RagiumBlocks.SPONGE_CAKE_SLAB)
            .addIngredient(RagiumBlocks.SPONGE_CAKE_SLAB)
            .saveSuffixed(output, "_with_sponge")

        HTShapelessRecipeBuilder(RagiumItems.MELON_PIE, 2)
            .addIngredient(Tags.Items.CROPS_MELON)
            .addIngredient(Items.SUGAR)
            .addIngredient(RagiumBlocks.SPONGE_CAKE_SLAB)
            .addIngredient(RagiumBlocks.SPONGE_CAKE_SLAB)
            .saveSuffixed(output, "_with_sponge")
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
            ).define('A', RagiumItemTags.FOOD_CHOCOLATE)
            .define('B', Items.SUGAR)
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
