package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.impl.HTShapedRecipeBuilder
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredHolder
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.CommonTags
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder

object RagiumDelightRecipeProvider : HTRecipeProvider.Integration(RagiumConst.FARMERS_DELIGHT) {
    override fun buildRecipeInternal() {
        // Milk
        extractAndInfuse(
            HTIngredientHelper.item(Items.GLASS_BOTTLE),
            ModItems.MILK_BOTTLE.get(),
            HTFluidContent.MILK,
            250,
        )

        cherry()
        cake()
    }

    private fun cherry() {
        cutting(RagiumCommonTags.Items.FOODS_RAGI_CHERRY, RagiumDelightAddon.RAGI_CHERRY_PULP, 2)
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
        // Pie
        HTShapedRecipeBuilder(RagiumDelightAddon.RAGI_CHERRY_PIE)
            .pattern(
                "AAA",
                "BBB",
                "CDC",
            ).define('A', Tags.Items.CROPS_WHEAT)
            .define('B', RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .define('C', Items.SUGAR)
            .define('D', ModItems.PIE_CRUST.get())
            .save(output)

        cutting(RagiumDelightAddon.RAGI_CHERRY_PIE, RagiumDelightAddon.RAGI_CHERRY_PIE_SLICE, 4)

        HTShapedRecipeBuilder(RagiumDelightAddon.RAGI_CHERRY_PIE)
            .storage4()
            .define('A', RagiumDelightAddon.RAGI_CHERRY_PIE_SLICE)
            .saveSuffixed(output, "_from_slice")
    }

    private fun cake() {
        cutting(RagiumBlocks.SWEET_BERRIES_CAKE, RagiumItems.SWEET_BERRIES_CAKE_SLICE, 7)
    }

    @JvmStatic
    private fun cutting(hole: TagKey<Item>, slice: ItemLike, count: Int) {
        CuttingBoardRecipeBuilder
            .cuttingRecipe(Ingredient.of(hole), Ingredient.of(CommonTags.TOOLS_KNIFE), slice, count)
            .save(output, hole.location)
    }

    @JvmStatic
    private fun cutting(hole: DeferredHolder<out ItemLike, *>, slice: ItemLike, count: Int) {
        CuttingBoardRecipeBuilder
            .cuttingRecipe(Ingredient.of(hole.get()), Ingredient.of(CommonTags.TOOLS_KNIFE), slice, count)
            .save(output, hole.id)
    }
}
