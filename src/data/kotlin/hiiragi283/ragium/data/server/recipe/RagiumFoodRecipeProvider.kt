package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
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
            .misc(RagiumItems.MELON_PIE)
            .addIngredient(Tags.Items.CROPS_MELON)
            .addIngredient(Items.SUGAR)
            .addIngredient(Tags.Items.EGGS)
            .save(output)

        // Ambrosia
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.AMBROSIA),
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, FoodMaterialKeys.CHOCOLATE, 64),
                itemCreator.fromItem(Items.HONEY_BLOCK, 64),
            ).save(output)

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
            .misc(RagiumItems.RAGI_CHERRY_JAM)
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
            .define('D', CommonMaterialPrefixes.DOUGH, FoodMaterialKeys.WHEAT)
            .save(output)

        cutAndCombine(RagiumItems.RAGI_CHERRY_PIE, RagiumItems.RAGI_CHERRY_PIE_SLICE, 4)
        // Ragi-Cherry Toast
        HTShapelessRecipeBuilder
            .misc(RagiumItems.RAGI_CHERRY_TOAST, 2)
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
            .define('D', HTDecorationVariant.SPONGE_CAKE.slab)
            .saveSuffixed(output, "_with_sponge")

        cutAndCombine(RagiumBlocks.SWEET_BERRIES_CAKE, RagiumItems.SWEET_BERRIES_CAKE_SLICE, 7)
    }

    @JvmStatic
    private fun cream() {
        // Milk -> Cream
        HTComplexRecipeBuilder
            .refining(
                fluidCreator.milk(1000),
                resultHelper.fluid(RagiumFluidContents.CREAM, 250),
                null,
                null,
            ).save(output)

        extractAndInfuse(Items.BOWL, RagiumItems.CREAM_BOWL, RagiumFluidContents.CREAM)
        // Cream -> Butter
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALT),
                fluidCreator.fromHolder(RagiumFluidContents.CREAM, 250),
            ).addResult(resultHelper.item(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.BUTTER))
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
            .define('D', HTDecorationVariant.SPONGE_CAKE.slab)
            .saveSuffixed(output, "_with_sponge")

        // Ice Cream
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.SNOWBALL),
                fluidCreator.milk(250),
            ).addResult(resultHelper.item(RagiumItems.ICE_CREAM))
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
            itemCreator.fromItem(RagiumItems.getMold(CommonMaterialPrefixes.STORAGE_BLOCK)),
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
        HTItemToObjRecipeBuilder
            .pulverizing(
                itemCreator.fromTagKey(RagiumModTags.Items.RAW_MEAT),
                resultHelper.item(CommonMaterialPrefixes.DUST, FoodMaterialKeys.RAW_MEAT),
            ).save(output)
        // Meat Ingot
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, FoodMaterialKeys.RAW_MEAT),
                resultHelper.item(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAW_MEAT),
            ).save(output)

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
        // Dough
        HTShapelessRecipeBuilder
            .misc(RagiumItems.getMaterial(CommonMaterialPrefixes.DOUGH, FoodMaterialKeys.WHEAT), 3)
            .addIngredients(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT, 3)
            .addIngredient(Tags.Items.BUCKETS_WATER)
            .save(output)

        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT),
                fluidCreator.water(250),
            ).addResult(resultHelper.item(CommonMaterialPrefixes.DOUGH, FoodMaterialKeys.WHEAT))
            .save(output)
        // Bread from dough
        HTCookingRecipeBuilder.smeltingAndSmoking(Items.BREAD) {
            addIngredient(CommonMaterialPrefixes.DOUGH, FoodMaterialKeys.WHEAT)
            saveSuffixed(output, "_from_dough")
        }
    }
}
