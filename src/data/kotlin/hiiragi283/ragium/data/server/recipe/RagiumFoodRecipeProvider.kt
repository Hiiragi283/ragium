package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.common.variant.HTDecorationVariant
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
                HTIngredientHelper.item(Tags.Items.MUSHROOMS, 2),
                HTIngredientHelper.milk(250),
                HTResultHelper.INSTANCE.fluid(RagiumFluidContents.MUSHROOM_STEW, 250),
            ).save(output)

        extractAndInfuse(
            HTIngredientHelper.item(Items.BOWL),
            Items.MUSHROOM_STEW,
            RagiumFluidContents.MUSHROOM_STEW,
            250,
        )

        // Chocolate
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.CROPS_COCOA_BEAN),
                HTIngredientHelper.milk(250),
                HTResultHelper.INSTANCE.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.CHOCOLATE),
            ).saveSuffixed(output, "_from_milk")

        HTFluidTransformRecipeBuilder
            .solidifying(
                null,
                HTIngredientHelper.fluid(RagiumCommonTags.Fluids.CHOCOLATES, 250),
                HTResultHelper.INSTANCE.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.CHOCOLATE),
            ).save(output)
        // Melon Pie
        HTShapelessRecipeBuilder
            .misc(RagiumItems.MELON_PIE)
            .addIngredient(Tags.Items.CROPS_MELON)
            .addIngredient(Items.SUGAR)
            .addIngredient(Tags.Items.EGGS)
            .save(output)

        // Ice Cream
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.SNOWBALL),
                HTIngredientHelper.milk(250),
                HTResultHelper.INSTANCE.item(RagiumItems.ICE_CREAM),
            ).save(output)
        // Ice Cream Soda
        save(
            RagiumAPI.id("shapeless/ice_cream_soda"),
            HTIceCreamSodaRecipe(CraftingBookCategory.MISC),
        )

        // Ambrosia
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.INSTANCE.item(RagiumItems.AMBROSIA),
                HTIngredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CHOCOLATE, 64),
                HTIngredientHelper.item(Items.HONEY_BLOCK, 64),
            ).save(output)

        cherry()
        honey()
        meat()
        sponge()
    }

    @JvmStatic
    private fun cherry() {
        // Ragi-Cherry
        HTShapedRecipeBuilder
            .misc(RagiumItems.RAGI_CHERRY, 8)
            .hollow8()
            .define('A', RagiumCommonTags.Items.FOODS_APPLE)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .save(output)
        // Fever Cherry
        HTShapedRecipeBuilder
            .misc(RagiumItems.FEVER_CHERRY)
            .hollow8()
            .define('A', HTBlockMaterialVariant.STORAGE_BLOCK, HTVanillaMaterialType.GOLD)
            .define('B', RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .save(output)
    }

    @JvmStatic
    private fun honey() {
        // Honey Block <-> Honey
        meltAndFreeze(
            HTIngredientHelper.item(Tags.Items.GLASS_BLOCKS),
            Items.HONEY_BLOCK,
            RagiumFluidContents.HONEY,
            1000,
        )
        // Honey Bottle <-> Honey
        extractAndInfuse(
            HTIngredientHelper.item(Items.GLASS_BOTTLE),
            Items.HONEY_BOTTLE,
            RagiumFluidContents.HONEY,
            250,
        )
    }

    @JvmStatic
    private fun meat() {
        // Minced Meat
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(RagiumModTags.Items.RAW_MEAT),
                HTResultHelper.INSTANCE.item(HTItemMaterialVariant.DUST, RagiumMaterialType.MEAT),
            ).save(output)
        // Meat Ingot
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.MEAT),
                HTResultHelper.INSTANCE.item(RagiumItems.getIngot(RagiumMaterialType.MEAT)),
            ).save(output)

        HTFluidTransformRecipeBuilder
            .solidifying(
                null,
                HTIngredientHelper.fluid(RagiumCommonTags.Fluids.MEAT, 250),
                HTResultHelper.INSTANCE.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.MEAT),
            ).save(output)

        HTCookingRecipeBuilder
            .smoking(RagiumItems.getIngot(RagiumMaterialType.COOKED_MEAT))
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.MEAT)
            .setExp(0.35f)
            .save(output)
        // Canned Cooked Meat
        HTShapedRecipeBuilder
            .misc(RagiumItems.CANNED_COOKED_MEAT, 8)
            .hollow8()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.COOKED_MEAT)
            .define('B', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
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
            ).define('A', RagiumCommonTags.Items.FOODS_CHOCOLATE)
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
}
