package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleExtraItemRecipeBuilder
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTCookingPotRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCuttingBoardRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumIntegrationItems
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.CommonTags

object RagiumDelightRecipeProvider : HTRecipeProvider.Integration(RagiumConst.FARMERS_DELIGHT) {
    override fun buildRecipeInternal() {
        // Milk
        extractAndInfuse(
            Items.GLASS_BOTTLE,
            HTItemHolderLike.fromItem(ModItems.MILK_BOTTLE),
            HTFluidHolderLike.MILK,
        )
        // Rich soil
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(ModItems.ORGANIC_COMPOST.get()))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.CRUDE_BIO, 250))
            .setResult(resultHelper.item(ModItems.RICH_SOIL.get()))
            .save(output)

        // Rice Panicle
        HTSingleExtraItemRecipeBuilder
            .crushing(
                itemCreator.fromItem(ModItems.RICE_PANICLE.get()),
                resultHelper.item(ModItems.RICE.get()),
                resultHelper.item(ModItems.STRAW.get()),
            ).save(output)

        // Crops
        cropAndSeed(ModItems.CABBAGE_SEEDS.get(), ModItems.CABBAGE.get())
        cropAndCrop(ModItems.ONION.get())
        cropAndSeed(ModItems.RICE.get(), ModItems.RICE_PANICLE.get(), 500)
        cropAndSeed(ModItems.TOMATO_SEEDS.get(), ModItems.TOMATO.get())
        // Knives
        mapOf(
            RagiumMaterialKeys.RAGI_ALLOY to CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.RAGI_CRYSTAL to CommonMaterialPrefixes.GEM,
        ).forEach { (key: HTMaterialKey, prefix: HTPrefixLike) ->
            HTShapedRecipeBuilder
                .create(RagiumIntegrationItems.getKnife(key))
                .pattern("A", "B")
                .define('A', prefix, key)
                .define('B', Tags.Items.RODS_WOODEN)
                .setCategory(CraftingBookCategory.EQUIPMENT)
                .save(output)
        }

        cherry()
        cake()

        cutting()
    }

    @JvmStatic
    private fun cherry() {
        cuttingFromData(FoodMaterialRecipeData.RAGI_CHERRY_PULP)
        cuttingFromData(FoodMaterialRecipeData.RAGI_CHERRY_PIE)
        // Jam
        HTCookingPotRecipeBuilder
            .create(RagiumItems.RAGI_CHERRY_JAM, container = Items.GLASS_BOTTLE)
            .addIngredient(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .addIngredient(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .addIngredient(Items.SUGAR)
            .setTab(CookingPotRecipeBookTab.MISC)
            .setExp(0.35f)
            .save(output)

        /*HTShapedRecipeBuilder
            .create(RagiumDelightContents.RAGI_CHERRY_TOAST_BLOCK)
            .pattern(
                "ABA",
                "ACA",
                "CDC",
            ).define('A', CommonMaterialPrefixes.JAM, FoodMaterialKeys.RAGI_CHERRY)
            .define('B', Tags.Items.DRINKS_HONEY)
            .define('C', Tags.Items.FOODS_BREAD)
            .define('D', Items.BOWL)
            .save(output)*/
    }

    @JvmStatic
    private fun cake() {
        cuttingFromData(FoodMaterialRecipeData.SWEET_BERRIES_CAKE_SLICE)
    }

    @JvmStatic
    private fun cutting() {
        mapOf(
            // Meat
            Items.BEEF to ModItems.MINCED_BEEF.get(),
            Items.PORKCHOP to ModItems.BACON.get(),
            Items.MUTTON to ModItems.MUTTON_CHOPS.get(),
            Items.COOKED_MUTTON to ModItems.COOKED_MUTTON_CHOPS.get(),
            // Vegetable
            ModItems.CABBAGE.get() to ModItems.CABBAGE_LEAF.get(),
        ).forEach { (full: Item, cut: Item) ->
            HTSingleExtraItemRecipeBuilder
                .cutting(
                    itemCreator.fromItem(full),
                    resultHelper.item(cut, 2),
                ).save(output)
        }

        HTSingleExtraItemRecipeBuilder
            .cutting(
                itemCreator.fromTagKey(Tags.Items.CROPS_PUMPKIN),
                resultHelper.item(ModItems.PUMPKIN_SLICE.get(), 4),
            ).save(output)

        HTSingleExtraItemRecipeBuilder
            .cutting(
                itemCreator.fromItem(ModItems.BROWN_MUSHROOM_COLONY.get()),
                resultHelper.item(Items.BROWN_MUSHROOM, 5),
            ).save(output)

        HTSingleExtraItemRecipeBuilder
            .cutting(
                itemCreator.fromItem(ModItems.RED_MUSHROOM_COLONY.get()),
                resultHelper.item(Items.RED_MUSHROOM, 5),
            ).save(output)

        HTSingleExtraItemRecipeBuilder
            .cutting(
                itemCreator.fromTagKey(RagiumCommonTags.Items.FOODS_DOUGH),
                resultHelper.item(ModItems.RAW_PASTA.get()),
            ).save(output)
        // with Bone Meal
        mapOf(
            // Meat
            Items.CHICKEN to ModItems.CHICKEN_CUTS.get(),
            // Fish
            Items.COD to ModItems.COD_SLICE.get(),
            Items.SALMON to ModItems.SALMON_SLICE.get(),
            Items.COOKED_COD to ModItems.COOKED_COD_SLICE.get(),
            Items.COOKED_SALMON to ModItems.COOKED_SALMON_SLICE.get(),
        ).forEach { (full: Item, cut: Item) ->
            HTSingleExtraItemRecipeBuilder
                .cutting(
                    itemCreator.fromItem(full),
                    resultHelper.item(cut, 2),
                    resultHelper.item(Items.BONE_MEAL),
                ).save(output)
        }
        // With Bone
        mapOf(
            ModItems.HAM.get() to Items.PORKCHOP,
            ModItems.SMOKED_HAM.get() to Items.COOKED_PORKCHOP,
        ).forEach { (ham: Item, cut: Item) ->
            HTSingleExtraItemRecipeBuilder
                .cutting(
                    itemCreator.fromItem(ham),
                    resultHelper.item(cut, 2),
                    resultHelper.item(Items.BONE),
                ).save(output)
        }
    }

    //    Extensions    //

    @JvmStatic
    private fun cuttingFromData(data: HTRecipeData) {
        val (output: ItemStack, chance: Float) = data.getItemStacks()[0]
        HTCuttingBoardRecipeBuilder(output.toImmutableOrThrow(), chance)
            .addIngredient(data.getIngredients()[0])
            .addIngredient(CommonTags.TOOLS_KNIFE)
            .saveModified(this.output, data.operator)
    }
}
