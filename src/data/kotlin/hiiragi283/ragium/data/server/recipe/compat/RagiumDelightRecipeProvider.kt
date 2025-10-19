package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.integration.RagiumDelightAddon
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.impl.data.recipe.HTCuttingBoardRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.CommonTags
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder

object RagiumDelightRecipeProvider : HTRecipeProvider.Integration(RagiumConst.FARMERS_DELIGHT) {
    override fun buildRecipeInternal() {
        // Milk
        extractAndInfuse(
            itemCreator.fromItem(Items.GLASS_BOTTLE),
            HTItemHolderLike.fromItem(ModItems.MILK_BOTTLE.get()),
            HTFluidContent.MILK,
            250,
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
            .equipment(RagiumDelightAddon.RAGI_ALLOY_KNIFE)
            .pattern("A", "B")
            .define('A', HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .save(output)

        createComponentUpgrade(
            HTComponentTier.ELITE,
            RagiumDelightAddon.RAGI_CRYSTAL_KNIFE,
            RagiumDelightAddon.RAGI_ALLOY_KNIFE,
        ).addIngredient(HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .save(output)
    }

    @JvmStatic
    private fun cherry() {
        HTCuttingBoardRecipeBuilder
            .create(RagiumDelightAddon.RAGI_CHERRY_PULP, 2)
            .addIngredient(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .addIngredient(CommonTags.TOOLS_KNIFE)
            .save(output)
        // Pie
        HTShapedRecipeBuilder
            .misc(RagiumDelightAddon.RAGI_CHERRY_PIE)
            .pattern(
                "AAA",
                "BBB",
                "CDC",
            ).define('A', Tags.Items.CROPS_WHEAT)
            .define('B', RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
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
        CookingPotRecipeBuilder
            .cookingPotRecipe(
                RagiumDelightAddon.RAGI_CHERRY_JAM,
                1,
                200,
                0.35f,
                Items.GLASS_BOTTLE,
            ).addIngredient(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .addIngredient(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .addIngredient(Items.SUGAR)
            .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
            .save(output)

        HTShapedRecipeBuilder
            .misc(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK)
            .pattern(
                "ABA",
                "ACA",
                "CDC",
            ).define('A', RagiumCommonTags.Items.JAMS_RAGI_CHERRY)
            .define('B', Tags.Items.DRINKS_HONEY)
            .define('C', Tags.Items.FOODS_BREAD)
            .define('D', Items.BOWL)
            .save(output)
    }

    @JvmStatic
    private fun cake() {
        HTCuttingBoardRecipeBuilder
            .create(RagiumItems.SWEET_BERRIES_CAKE_SLICE, 7)
            .addIngredient(RagiumBlocks.SWEET_BERRIES_CAKE)
            .addIngredient(CommonTags.TOOLS_KNIFE)
            .save(output)
    }
}
