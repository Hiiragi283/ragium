package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTCookingPotRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCuttingBoardRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumIntegrationItems
import hiiragi283.ragium.setup.RagiumItems
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
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(ModItems.ORGANIC_COMPOST.get()),
                fluidCreator.fromHolder(RagiumFluidContents.ORGANIC_MUTAGEN, 250),
            ).addResult(resultHelper.item(ModItems.RICH_SOIL.get()))
            .save(output)

        // Rice Panicle
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromItem(ModItems.RICE_PANICLE.get()))
            .addResult(resultHelper.item(ModItems.RICE.get()))
            .addResult(resultHelper.item(ModItems.STRAW.get()), 0.5f)
            .save(output)

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
    private fun cuttingFromData(data: HTRecipeData) {
        val (output: ItemStack, chance: Float) = data.getItemStacks()[0]
        HTCuttingBoardRecipeBuilder(output.toImmutableOrThrow(), chance)
            .addIngredient(data.getIngredients()[0])
            .addIngredient(CommonTags.TOOLS_KNIFE)
            .saveModified(this.output, data.operator)
    }
}
