package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapelessRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

object RagiumFoodRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Mushroom Stew
        HTFluidTransformRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(Tags.Items.MUSHROOMS, 2),
                fluidCreator.milk(250),
                resultHelper.fluid(RagiumFluidContents.MUSHROOM_STEW, 250),
            ).save(output)

        extractAndInfuse(
            itemCreator.fromItem(Items.BOWL),
            Items.MUSHROOM_STEW.toHolderLike(),
            RagiumFluidContents.MUSHROOM_STEW,
        )

        // Chocolate
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(Tags.Items.CROPS_COCOA_BEAN),
                fluidCreator.milk(250),
            ).addResult(resultHelper.item(CommonMaterialPrefixes.INGOT, FoodMaterialKeys.CHOCOLATE))
            .saveSuffixed(output, "_from_milk")

        HTFluidTransformRecipeBuilder
            .solidifying(
                null,
                fluidCreator.fromTagKey(RagiumCommonTags.Fluids.CHOCOLATES, 250),
                resultHelper.item(CommonMaterialPrefixes.INGOT, FoodMaterialKeys.CHOCOLATE),
            ).save(output)
        // Melon Pie
        HTShapelessRecipeBuilder
            .misc(RagiumItems.MELON_PIE)
            .addIngredient(Tags.Items.CROPS_MELON)
            .addIngredient(Items.SUGAR)
            .addIngredient(Tags.Items.EGGS)
            .save(output)

        // Ice Cream
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(Items.SNOWBALL),
                fluidCreator.milk(250),
            ).addResult(resultHelper.item(RagiumItems.ICE_CREAM))
            .save(output)
        // Ice Cream Soda
        save(
            RagiumAPI.id("shapeless/ice_cream_soda"),
            HTIceCreamSodaRecipe(CraftingBookCategory.MISC),
        )

        // Ambrosia
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(RagiumItems.AMBROSIA),
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, FoodMaterialKeys.CHOCOLATE, 64),
                itemCreator.fromItem(Items.HONEY_BLOCK, 64),
            ).save(output)

        cherry()
        honey()
        meat()
        sponge()
        wheat()
    }

    @JvmStatic
    private fun cherry() {
        // Ragi-Cherry
        HTShapedRecipeBuilder
            .misc(RagiumItems.RAGI_CHERRY, 8)
            .hollow8()
            .define('A', CommonMaterialPrefixes.FOOD, FoodMaterialKeys.APPLE)
            .define('B', CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE)
            .save(output)
        // Ragi-Cherry Jam
        HTShapelessRecipeBuilder
            .misc(RagiumItems.RAGI_CHERRY_JAM)
            .addIngredient(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .addIngredient(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .addIngredient(Items.SUGAR)
            .addIngredient(Items.GLASS_BOTTLE)
            .save(output)
        // Ragi-Cherry Toast
        HTShapelessRecipeBuilder
            .misc(RagiumItems.RAGI_CHERRY_TOAST, 2)
            .addIngredient(Tags.Items.FOODS_BREAD)
            .addIngredient(Tags.Items.FOODS_BREAD)
            .addIngredient(CommonMaterialPrefixes.JAM, FoodMaterialKeys.RAGI_CHERRY)
            .save(output)
        // Fever Cherry
        HTShapedRecipeBuilder
            .misc(RagiumItems.FEVER_CHERRY)
            .hollow8()
            .define('A', CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.GOLD)
            .define('B', CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .save(output)
    }

    @JvmStatic
    private fun honey() {
        // Honey Block <-> Honey
        meltAndFreeze(
            itemCreator.fromTagKey(Tags.Items.GLASS_BLOCKS),
            Items.HONEY_BLOCK.toHolderLike(),
            RagiumFluidContents.HONEY,
            1000,
        )
        // Honey Bottle <-> Honey
        extractAndInfuse(
            itemCreator.fromItem(Items.GLASS_BOTTLE),
            Items.HONEY_BOTTLE.toHolderLike(),
            RagiumFluidContents.HONEY,
        )
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
                resultHelper.item(CommonMaterialPrefixes.INGOT, FoodMaterialKeys.RAW_MEAT),
            ).save(output)

        HTFluidTransformRecipeBuilder
            .solidifying(
                null,
                fluidCreator.fromContent(RagiumFluidContents.MEAT, 250),
                resultHelper.item(CommonMaterialPrefixes.INGOT, FoodMaterialKeys.RAW_MEAT),
            ).save(output)

        HTCookingRecipeBuilder
            .smeltingAndSmoking(RagiumItems.getIngot(FoodMaterialKeys.COOKED_MEAT)) {
                addIngredient(CommonMaterialPrefixes.INGOT, FoodMaterialKeys.RAW_MEAT)
                setExp(0.35f)
                save(output)
            }
        // Canned Cooked Meat
        HTShapedRecipeBuilder
            .misc(RagiumItems.CANNED_COOKED_MEAT, 8)
            .hollow8()
            .define('A', CommonMaterialPrefixes.INGOT, FoodMaterialKeys.COOKED_MEAT)
            .define('B', CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.IRON)
            .save(output)
    }

    @JvmStatic
    private fun sponge() {
        // Cakes
        HTShapedRecipeBuilder
            .misc(Items.CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', Tags.Items.BUCKETS_MILK)
            .define('B', Items.SUGAR)
            .define('C', Tags.Items.EGGS)
            .define('D', HTDecorationVariant.SPONGE_CAKE.slab)
            .saveSuffixed(output, "_with_sponge")

        HTShapedRecipeBuilder
            .misc(RagiumBlocks.SWEET_BERRIES_CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', CommonMaterialPrefixes.FOOD, FoodMaterialKeys.CHOCOLATE)
            .define('B', Tags.Items.FOODS_BERRY)
            .define('C', Tags.Items.EGGS)
            .define('D', HTDecorationVariant.SPONGE_CAKE.slab)
            .saveSuffixed(output, "_with_sponge")

        HTShapelessRecipeBuilder
            .misc(RagiumItems.SWEET_BERRIES_CAKE_SLICE, 7)
            .addIngredient(RagiumBlocks.SWEET_BERRIES_CAKE)
            .save(output)

        HTShapelessRecipeBuilder
            .misc(RagiumBlocks.SWEET_BERRIES_CAKE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addIngredient(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .save(output)
    }

    @JvmStatic
    private fun wheat() {
        // Dough
        HTShapelessRecipeBuilder
            .misc(RagiumItems.getMaterial(CommonMaterialPrefixes.DOUGH, FoodMaterialKeys.WHEAT), 3)
            .addIngredient(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT)
            .addIngredient(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT)
            .addIngredient(CommonMaterialPrefixes.FLOUR, FoodMaterialKeys.WHEAT)
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
