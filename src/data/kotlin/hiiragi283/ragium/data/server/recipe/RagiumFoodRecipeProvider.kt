package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidWithCatalystToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTDecorationVariant
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.CompoundIngredient

object RagiumFoodRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Mushroom Stew
        HTFluidTransformRecipeBuilder
            .mixing(
                HTIngredientHelper.item(Tags.Items.MUSHROOMS, 2),
                HTIngredientHelper.milk(250),
                HTResultHelper.fluid(RagiumFluidContents.MUSHROOM_STEW, 250),
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
                HTResultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.CHOCOLATE),
            ).saveSuffixed(output, "_from_milk")

        HTFluidWithCatalystToObjRecipeBuilder
            .solidifying(
                null,
                HTIngredientHelper.fluid(RagiumCommonTags.Fluids.CHOCOLATES, 250),
                HTResultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.CHOCOLATE),
            ).save(output)
        // Melon Pie
        HTShapelessRecipeBuilder(RagiumItems.MELON_PIE)
            .addIngredient(Tags.Items.CROPS_MELON)
            .addIngredient(Items.SUGAR)
            .addIngredient(Tags.Items.EGGS)
            .save(output)

        // Ice Cream
        HTFluidTransformRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.SNOWBALL),
                HTIngredientHelper.milk(250),
                HTResultHelper.item(RagiumItems.ICE_CREAM),
            ).save(output)
        // Ice Cream Soda
        save(
            RagiumAPI.id("shapeless/ice_cream_soda"),
            HTIceCreamSodaRecipe(CraftingBookCategory.MISC),
        )

        // Ambrosia
        HTCombineItemToObjRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.AMBROSIA),
                HTIngredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CHOCOLATE, 64),
                HTIngredientHelper.item(Items.HONEY_BLOCK, 64),
            ).save(output)

        cherry()
        honey()
        meat()
        sponge()
    }

    @_root_ide_package_.kotlin.jvm.JvmStatic
    private fun cherry() {
        // Ragi-Cherry
        HTShapedRecipeBuilder(RagiumItems.RAGI_CHERRY, 8)
            .hollow8()
            .define('A', RagiumCommonTags.Items.FOODS_APPLE)
            .define('B', HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE)
            .save(output)
        // Fever Cherry
        HTShapedRecipeBuilder(RagiumItems.FEVER_CHERRY)
            .hollow8()
            .define('A', HTBlockMaterialVariant.STORAGE_BLOCK, HTVanillaMaterialType.GOLD)
            .define('B', RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .save(output)
    }

    @_root_ide_package_.kotlin.jvm.JvmStatic
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

    @_root_ide_package_.kotlin.jvm.JvmStatic
    private fun meat() {
        // Minced Meat
        HTItemToObjRecipeBuilder
            .pulverizing(
                HTIngredientHelper.item(
                    CompoundIngredient.of(
                        Ingredient.of(Tags.Items.FOODS_RAW_MEAT),
                        Ingredient.of(Tags.Items.FOODS_RAW_FISH),
                        Ingredient.of(Items.ROTTEN_FLESH),
                    ),
                ),
                HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.MEAT),
            ).save(output)
        // Meat Ingot
        HTShapedRecipeBuilder(RagiumItems.getIngot(RagiumMaterialType.MEAT), 3)
            .pattern("AAA")
            .define('A', HTItemMaterialVariant.DUST, RagiumMaterialType.MEAT)
            .save(output)

        HTFluidWithCatalystToObjRecipeBuilder
            .solidifying(
                null,
                HTIngredientHelper.fluid(RagiumCommonTags.Fluids.MEAT, 250),
                HTResultHelper.item(HTItemMaterialVariant.INGOT, RagiumMaterialType.MEAT),
            ).save(output)

        HTCookingRecipeBuilder
            .smoking(RagiumItems.getIngot(RagiumMaterialType.COOKED_MEAT))
            .addIngredient(HTItemMaterialVariant.INGOT, RagiumMaterialType.MEAT)
            .setExp(0.35f)
            .save(output)
        // Canned Cooked Meat
        HTShapedRecipeBuilder(RagiumItems.CANNED_COOKED_MEAT, 8)
            .hollow8()
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.COOKED_MEAT)
            .define('B', HTItemMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
            .save(output)
    }

    @JvmStatic
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
            .define('D', HTDecorationVariant.SPONGE_CAKE.slab)
            .saveSuffixed(output, "_with_sponge")

        HTShapedRecipeBuilder(RagiumBlocks.SWEET_BERRIES_CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', RagiumCommonTags.Items.FOODS_CHOCOLATE)
            .define('B', Tags.Items.FOODS_BERRY)
            .define('C', Tags.Items.EGGS)
            .define('D', HTDecorationVariant.SPONGE_CAKE.slab)
            .saveSuffixed(output, "_with_sponge")

        HTShapelessRecipeBuilder(RagiumItems.SWEET_BERRIES_CAKE_SLICE, 7)
            .addIngredient(RagiumBlocks.SWEET_BERRIES_CAKE)
            .save(output)

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
