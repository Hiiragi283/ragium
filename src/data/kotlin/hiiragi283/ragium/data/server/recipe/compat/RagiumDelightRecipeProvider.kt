package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.common.integration.RagiumDelightAddon
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.impl.data.recipe.HTCookingPotRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTCuttingBoardRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.FoodMaterialRecipeData
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.CommonTags

object RagiumDelightRecipeProvider : HTRecipeProvider.Integration(RagiumConst.FARMERS_DELIGHT) {
    override fun buildRecipeInternal() {
        // Milk
        extractAndInfuse(
            Items.GLASS_BOTTLE,
            ModItems.MILK_BOTTLE.toHolderLike(),
            HTFluidContent.MILK,
        )
        // Rich soil
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(ModItems.ORGANIC_COMPOST.get()),
                fluidCreator.fromContent(RagiumFluidContents.ORGANIC_MUTAGEN, 250),
            ).addResult(resultHelper.item(ModItems.RICH_SOIL.get()))
            .save(output)

        // Rice Panicle
        HTItemToChancedItemRecipeBuilder
            .crushing(itemCreator.fromItem(ModItems.RICE_PANICLE.get()))
            .addResult(resultHelper.item(ModItems.RICE.get()))
            .addResult(resultHelper.item(ModItems.STRAW.get()), 0.5f)
            .save(output)

        knife()
        cherry()
        cake()
    }

    @JvmStatic
    private fun knife() {
        HTShapedRecipeBuilder
            .equipment(RagiumDelightAddon.getKnife(RagiumMaterialKeys.RAGI_ALLOY))
            .pattern("A", "B")
            .define('A', CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .save(output)

        createComponentUpgrade(
            HTComponentTier.ELITE,
            RagiumDelightAddon.getKnife(RagiumMaterialKeys.RAGI_CRYSTAL),
            RagiumDelightAddon.getKnife(RagiumMaterialKeys.RAGI_ALLOY),
        ).addIngredient(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .save(output)
    }

    @JvmStatic
    private fun cherry() {
        cuttingFromData(FoodMaterialRecipeData.RAGI_CHERRY_PULP)
        // Pie
        HTShapedRecipeBuilder
            .misc(RagiumDelightAddon.RAGI_CHERRY_PIE)
            .pattern(
                "AAA",
                "BBB",
                "CDC",
            ).define('A', Tags.Items.CROPS_WHEAT)
            .define('B', CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .define('C', Items.SUGAR)
            .define('D', ModItems.PIE_CRUST.get())
            .save(output)

        HTCuttingBoardRecipeBuilder
            .create(RagiumDelightAddon.RAGI_CHERRY_PIE_SLICE, 4)
            .addIngredient(RagiumDelightAddon.RAGI_CHERRY_PIE)
            .addIngredient(CommonTags.TOOLS_KNIFE)
            .save(output)

        HTShapedRecipeBuilder
            .misc(RagiumDelightAddon.RAGI_CHERRY_PIE)
            .storage4()
            .define('A', RagiumDelightAddon.RAGI_CHERRY_PIE_SLICE)
            .saveSuffixed(output, "_from_slice")
        // Jam
        HTCookingPotRecipeBuilder
            .create(RagiumItems.RAGI_CHERRY_JAM, container = Items.GLASS_BOTTLE)
            .addIngredient(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .addIngredient(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAGI_CHERRY)
            .addIngredient(Items.SUGAR)
            .setTab(CookingPotRecipeBookTab.MISC)
            .setExp(0.35f)
            .save(output)

        HTShapedRecipeBuilder
            .misc(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK)
            .pattern(
                "ABA",
                "ACA",
                "CDC",
            ).define('A', CommonMaterialPrefixes.JAM, FoodMaterialKeys.RAGI_CHERRY)
            .define('B', Tags.Items.DRINKS_HONEY)
            .define('C', Tags.Items.FOODS_BREAD)
            .define('D', Items.BOWL)
            .save(output)
    }

    @JvmStatic
    private fun cake() {
        cuttingFromData(FoodMaterialRecipeData.SWEET_BERRIES_CAKE_SLICE)
    }

    @JvmStatic
    private fun cuttingFromData(data: HTRecipeData) {
        val output: HTRecipeData.OutputEntry = data.outputs[0]
        HTCuttingBoardRecipeBuilder(output.toImmutable(), output.chance)
            .addIngredient(data.getIngredient(0))
            .addIngredient(CommonTags.TOOLS_KNIFE)
            .save(this.output)
    }
}
