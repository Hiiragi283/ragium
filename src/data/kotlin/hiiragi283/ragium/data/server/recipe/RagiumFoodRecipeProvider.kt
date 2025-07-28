package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.recipe.custom.HTIceCreamSodaRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition

object RagiumFoodRecipeProvider : HTRecipeProvider() {
    val disabledByDelight: ICondition = not(modLoaded(RagiumConst.FARMERS_DELIGHT))

    override fun buildRecipeInternal() {
        // Chocolate
        createSolidifying()
            .itemOutput(RagiumCommonTags.Items.INGOTS_CHOCOLATE)
            .catalyst(Tags.Items.CROPS_COCOA_BEAN)
            .milkInput(250)
            .saveSuffixed(output, "_from_milk")

        createSolidifying()
            .itemOutput(RagiumCommonTags.Items.INGOTS_CHOCOLATE)
            .fluidInput(RagiumCommonTags.Fluids.CHOCOLATES, 250)
            .save(output)
        // Melon Pie
        HTShapelessRecipeBuilder(RagiumItems.MELON_PIE)
            .addIngredient(Tags.Items.CROPS_MELON)
            .addIngredient(Items.SUGAR)
            .addIngredient(Tags.Items.EGGS)
            .save(output)

        // Ice Cream Soda
        save(
            RagiumAPI.id("shapeless/ice_cream_soda"),
            HTIceCreamSodaRecipe(CraftingBookCategory.MISC),
        )

        // Ambrosia
        createAlloying()
            .itemOutput(RagiumItems.AMBROSIA)
            .itemInput(RagiumCommonTags.Items.STORAGE_BLOCKS_CHOCOLATE, 64)
            .itemInput(Items.HONEY_BLOCK, 64)
            .save(output)

        cherry()
        honey()
        meat()
        sponge()
    }

    private fun cherry() {
        // Cherry Jam
        HTShapelessRecipeBuilder(RagiumItems.RAGI_CHERRY_JAM)
            .addIngredient(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .addIngredient(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .addIngredient(Items.SUGAR)
            .addIngredient(Items.GLASS_BOTTLE)
            .save(output.withConditions(disabledByDelight))
        // Fever Cherry
        HTShapedRecipeBuilder(RagiumItems.FEVER_CHERRY)
            .hollow8()
            .define('A', Tags.Items.STORAGE_BLOCKS_GOLD)
            .define('B', RagiumItems.RAGI_CHERRY)
            .save(output)
    }

    private fun honey() {
        // Beehive
        createCrushing()
            .itemOutput(Items.HONEYCOMB)
            .catalyst(Tags.Items.TOOLS_SHEAR)
        // .save(output)

        createCrushing()
            .itemOutput(Items.HONEY_BOTTLE)
            .itemInput(Items.GLASS_BOTTLE)
            .save(output)
    }

    private fun meat() {
        // Minced Meat
        createCrushing()
            .itemOutput(RagiumCommonTags.Items.DUSTS_MEAT)
            .itemInput(Tags.Items.FOODS_RAW_MEAT)
            .saveSuffixed(output, "_from_meat")
        createCrushing()
            .itemOutput(RagiumCommonTags.Items.DUSTS_MEAT)
            .itemInput(Tags.Items.FOODS_RAW_FISH)
            .saveSuffixed(output, "_from_fish")
        createCrushing()
            .itemOutput(RagiumCommonTags.Items.DUSTS_MEAT)
            .itemInput(Items.ROTTEN_FLESH)
            .saveSuffixed(output, "_from_rotten")
        // Meat Ingot
        HTShapedRecipeBuilder(RagiumItems.MEAT_INGOT, 3)
            .pattern("AAA")
            .define('A', RagiumCommonTags.Items.DUSTS_MEAT)
            .save(output)

        createSolidifying()
            .itemOutput(RagiumCommonTags.Items.INGOTS_MEAT)
            .fluidInput(RagiumCommonTags.Fluids.MEAT, 250)
            .save(output)

        HTCookingRecipeBuilder
            .smoking(RagiumItems.COOKED_MEAT_INGOT)
            .addIngredient(RagiumCommonTags.Items.INGOTS_MEAT)
            .setExp(0.35f)
            .save(output)
        // Canned Cooked Meat
        HTShapedRecipeBuilder(RagiumItems.CANNED_COOKED_MEAT, 8)
            .hollow8()
            .define('A', RagiumCommonTags.Items.INGOTS_COOKED_MEAT)
            .define('B', Tags.Items.INGOTS_IRON)
            .save(output)
    }

    private fun sponge() {
        // Cakes
        HTShapedRecipeBuilder(Items.CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', Tags.Items.BUCKETS_MILK)
            .define('B', Items.SUGAR)
            .define('C', Tags.Items.EGGS)
            .define('D', RagiumBlocks.SPONGE_CAKE_SETS.slab)
            .saveSuffixed(output, "_with_sponge")

        HTShapedRecipeBuilder(RagiumBlocks.SWEET_BERRIES_CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', RagiumCommonTags.Items.FOODS_CHOCOLATE)
            .define('B', Tags.Items.FOODS_BERRY)
            .define('C', Tags.Items.EGGS)
            .define('D', RagiumBlocks.SPONGE_CAKE_SETS.slab)
            .saveSuffixed(output, "_with_sponge")

        HTShapelessRecipeBuilder(RagiumItems.SWEET_BERRIES_CAKE_SLICE, 7)
            .addIngredient(RagiumBlocks.SWEET_BERRIES_CAKE)
            .save(output.withConditions(disabledByDelight))

        HTShapelessRecipeBuilder(RagiumBlocks.SWEET_BERRIES_CAKE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .save(output)
    }
}
