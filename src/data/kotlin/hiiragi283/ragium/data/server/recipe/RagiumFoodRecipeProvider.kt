package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.HTDecorationType
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.recipe.crafting.HTIceCreamSodaRecipe
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTSingleExtraItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object RagiumFoodRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Mushroom Stew
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.MUSHROOMS, 2))
            .addIngredient(fluidCreator.milk(250))
            .setResult(resultHelper.fluid(RagiumFluidContents.MUSHROOM_STEW, 250))
            .save(output)

        extractAndInfuse(Items.BOWL, Items.MUSHROOM_STEW.toHolderLike(), RagiumFluidContents.MUSHROOM_STEW)

        // Melon Pie
        HTShapelessRecipeBuilder
            .create(RagiumItems.MELON_PIE)
            .addIngredient(Tags.Items.CROPS_MELON)
            .addIngredient(Items.SUGAR)
            .addIngredient(Tags.Items.EGGS)
            .save(output)

        // Ambrosia
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, FoodMaterialKeys.CHOCOLATE, 64))
            .addIngredient(itemCreator.fromItem(Items.HONEY_BLOCK, 64))
            .addIngredient(itemCreator.fromItem(RagiumItems.IRIDESCENT_POWDER))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CREAM, 8000))
            .setResult(resultHelper.item(RagiumItems.AMBROSIA))
            .save(output)

        cherry()
        chocolate()
        cream()
        honey()
        meat()
        wheat()
    }

    @JvmStatic
    private fun cherry() {
        // Ragi-Cherry
        HTShapedRecipeBuilder
            .create(RagiumItems.RAGI_CHERRY, 8)
            .hollow8()
            .define('A', CommonMaterialPrefixes.FOOD, FoodMaterialKeys.APPLE)
            .define('B', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .save(output)
        // Ragi-Cherry Juice
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY),
                null,
                fluidResult = resultHelper.fluid(RagiumFluidContents.RAGI_CHERRY_JUICE, 125),
            ).save(output)

        extractAndInfuse(Items.GLASS_BOTTLE, RagiumItems.RAGI_CHERRY_JUICE, RagiumFluidContents.RAGI_CHERRY_JUICE)
        // Ragi-Cherry Jam
        HTShapelessRecipeBuilder
            .create(RagiumItems.RAGI_CHERRY_JAM)
            .addIngredients(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY, 2)
            .addIngredient(Items.SUGAR)
            .addIngredient(Items.GLASS_BOTTLE)
            .addCondition(FOOD_MOD_CONDITION)
            .save(output)
        // Ragi-Cherry Pie
        HTShapedRecipeBuilder
            .create(RagiumItems.RAGI_CHERRY_PIE)
            .pattern(
                "AAA",
                "BBB",
                "CDC",
            ).define('A', CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT)
            .define('B', CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .define('C', Items.SUGAR)
            .define('D', RagiumCommonTags.Items.FOODS_DOUGH)
            .save(output)

        cutAndCombine(RagiumItems.RAGI_CHERRY_PIE, RagiumItems.RAGI_CHERRY_PIE_SLICE, 4)
        // Ragi-Cherry Toast
        HTShapelessRecipeBuilder
            .create(RagiumItems.RAGI_CHERRY_TOAST, 2)
            .addIngredients(Tags.Items.FOODS_BREAD, 2)
            .addIngredient(CommonMaterialPrefixes.JAM, FoodMaterialKeys.RAGI_CHERRY)
            .save(output)
        // Fever Cherry
        HTShapedRecipeBuilder
            .create(RagiumItems.FEVER_CHERRY)
            .hollow8()
            .define('A', CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.GOLD)
            .define('B', CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .save(output)
    }

    @JvmStatic
    private fun chocolate() {
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.CROPS_COCOA_BEAN))
            .addIngredient(fluidCreator.milk(250))
            .setResult(resultHelper.fluid(RagiumFluidContents.CHOCOLATE, 250))
            .save(output)

        meltAndFreeze(FoodMaterialRecipeData.CHOCOLATE_INGOT)
        // Cake
        HTShapedRecipeBuilder
            .create(RagiumBlocks.SWEET_BERRIES_CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', CommonMaterialPrefixes.FOOD, FoodMaterialKeys.CHOCOLATE)
            .define('B', Tags.Items.FOODS_BERRY)
            .define('C', Tags.Items.EGGS)
            .define('D', HTDecorationType.SPONGE_CAKE.slab)
            .saveSuffixed(output, "_with_sponge")

        cutAndCombine(RagiumBlocks.SWEET_BERRIES_CAKE, RagiumItems.SWEET_BERRIES_CAKE_SLICE, 7)
    }

    @JvmStatic
    private fun cream() {
        // Milk -> Cream
        distillation(
            HTFluidHolderLike.MILK to 1000,
            null,
            resultHelper.fluid(RagiumFluidContents.CREAM, 250) to null,
        )

        extractAndInfuse(Items.BOWL, RagiumItems.CREAM_BOWL, RagiumFluidContents.CREAM)
        // Cream -> Butter
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALT))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CREAM, 250))
            .setResult(resultHelper.item(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.BUTTER))
            .save(output)

        // Cake
        HTShapedRecipeBuilder
            .create(Items.CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', Tags.Items.BUCKETS_MILK)
            .define('B', Items.SUGAR)
            .define('C', Tags.Items.EGGS)
            .define('D', HTDecorationType.SPONGE_CAKE.slab)
            .saveSuffixed(output, "_with_sponge")

        // Ice Cream
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(Items.SNOWBALL))
            .addIngredient(fluidCreator.milk(250))
            .setResult(resultHelper.item(RagiumItems.ICE_CREAM))
            .save(output)
        // Ice Cream Soda
        save(
            RagiumAPI.id("shapeless", "ice_cream_soda"),
            HTIceCreamSodaRecipe(CraftingBookCategory.MISC),
        )
    }

    @JvmStatic
    private fun honey() {
        // Honey Block <-> Honey
        meltAndFreeze(
            HTMoldType.STORAGE_BLOCK,
            Items.HONEY_BLOCK.toHolderLike(),
            RagiumFluidContents.HONEY,
            1000,
        )
        // Honey Bottle <-> Honey
        extractAndInfuse(Items.GLASS_BOTTLE, Items.HONEY_BOTTLE.toHolderLike(), RagiumFluidContents.HONEY)
    }

    @JvmStatic
    private fun meat() {
        // Minced Meat
        HTSingleExtraItemRecipeBuilder
            .crushing(
                itemCreator.fromTagKey(RagiumModTags.Items.RAW_MEAT),
                resultHelper.item(CommonMaterialPrefixes.DUST, FoodMaterialKeys.RAW_MEAT),
            ).save(output)
        // Meat Ingot
        compressingTo(HTMoldType.INGOT, FoodMaterialKeys.RAW_MEAT, CommonMaterialPrefixes.DUST, outputPrefix = CommonMaterialPrefixes.FOOD)

        HTCookingRecipeBuilder
            .smeltingAndSmoking(RagiumItems.getFood(FoodMaterialKeys.COOKED_MEAT)) {
                addIngredient(RagiumItems.getFood(FoodMaterialKeys.RAW_MEAT))
                setExp(0.35f)
                save(output)
            }
        // Canned Cooked Meat
        HTShapedRecipeBuilder
            .create(RagiumItems.CANNED_COOKED_MEAT, 8)
            .hollow8()
            .define('A', CommonMaterialPrefixes.FOOD, FoodMaterialKeys.COOKED_MEAT)
            .define('B', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.IRON)
            .save(output)
    }

    @JvmStatic
    private fun wheat() {
        val dough: ItemLike = RagiumItems.getMaterial(CommonMaterialPrefixes.DOUGH, FoodMaterialKeys.WHEAT)
        // Dough
        HTShapelessRecipeBuilder
            .create(dough, 3)
            .addIngredients(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT, 3)
            .addIngredient(Tags.Items.BUCKETS_WATER)
            .save(output)

        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT))
            .addIngredient(fluidCreator.water(250))
            .setResult(resultHelper.item(dough))
            .save(output)
        // Bread from dough
        HTCookingRecipeBuilder.smeltingAndSmoking(Items.BREAD) {
            addIngredient(dough)
            saveSuffixed(output, "_from_dough")
        }
    }
}
