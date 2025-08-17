package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidWithCatalystToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTShapelessRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.common.recipe.HTIceCreamSodaRecipe
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.crafting.CompoundIngredient

object RagiumFoodRecipeProvider : HTRecipeProvider.Direct() {
    val disabledByDelight: ICondition = not(modLoaded(RagiumConst.FARMERS_DELIGHT))

    override fun buildRecipeInternal() {
        // Chocolate
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Tags.Items.CROPS_COCOA_BEAN),
                HTIngredientHelper.milk(250),
                HTResultHelper.item(HTMaterialVariant.INGOT, RagiumMaterialType.CHOCOLATE),
            ).saveSuffixed(output, "_from_milk")

        HTFluidWithCatalystToObjRecipeBuilder
            .solidifying(
                null,
                HTIngredientHelper.fluid(RagiumCommonTags.Fluids.CHOCOLATES, 250),
                HTResultHelper.item(HTMaterialVariant.INGOT, RagiumMaterialType.CHOCOLATE),
            ).save(output)
        // Melon Pie
        HTShapelessRecipeBuilder(RagiumItems.MELON_PIE)
            .addIngredient(Tags.Items.CROPS_MELON)
            .addIngredient(Items.SUGAR)
            .addIngredient(Tags.Items.EGGS)
            .save(output)

        // Ice Cream
        HTItemWithFluidToObjRecipeBuilder
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
        HTCombineItemToItemRecipeBuilder
            .alloying(
                HTResultHelper.item(RagiumItems.AMBROSIA),
                HTIngredientHelper.item(HTMaterialVariant.STORAGE_BLOCK, RagiumMaterialType.CHOCOLATE, 64),
                HTIngredientHelper.item(Items.HONEY_BLOCK, 64),
            ).save(output)

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
            .define('A', HTMaterialVariant.STORAGE_BLOCK, HTVanillaMaterialType.GOLD)
            .define('B', RagiumItems.RAGI_CHERRY)
            .save(output)
    }

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
                HTResultHelper.item(HTMaterialVariant.DUST, RagiumMaterialType.MEAT),
            ).save(output)
        // Meat Ingot
        HTShapedRecipeBuilder(RagiumItems.getIngot(RagiumMaterialType.MEAT), 3)
            .pattern("AAA")
            .define('A', HTMaterialVariant.DUST, RagiumMaterialType.MEAT)
            .save(output)

        HTFluidWithCatalystToObjRecipeBuilder
            .solidifying(
                null,
                HTIngredientHelper.fluid(RagiumCommonTags.Fluids.MEAT, 250),
                HTResultHelper.item(HTMaterialVariant.INGOT, RagiumMaterialType.MEAT),
            ).save(output)

        HTCookingRecipeBuilder
            .smoking(RagiumItems.getIngot(RagiumMaterialType.COOKED_MEAT))
            .addIngredient(HTMaterialVariant.INGOT, RagiumMaterialType.MEAT)
            .setExp(0.35f)
            .save(output)
        // Canned Cooked Meat
        HTShapedRecipeBuilder(RagiumItems.CANNED_COOKED_MEAT, 8)
            .hollow8()
            .define('A', HTMaterialVariant.INGOT, RagiumMaterialType.COOKED_MEAT)
            .define('B', HTMaterialVariant.INGOT, HTVanillaMaterialType.IRON)
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
            .define('D', RagiumBlocks.Slabs.SPONGE_CAKE)
            .saveSuffixed(output, "_with_sponge")

        HTShapedRecipeBuilder(RagiumBlocks.SWEET_BERRIES_CAKE)
            .pattern(
                " A ",
                "BCB",
                " D ",
            ).define('A', RagiumCommonTags.Items.FOODS_CHOCOLATE)
            .define('B', Tags.Items.FOODS_BERRY)
            .define('C', Tags.Items.EGGS)
            .define('D', RagiumBlocks.Slabs.SPONGE_CAKE)
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
