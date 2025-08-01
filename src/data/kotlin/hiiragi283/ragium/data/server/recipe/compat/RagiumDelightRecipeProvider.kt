package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.CommonTags
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder

object RagiumDelightRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        crafting()
        cookingPot()
        cutting()

        // Milk Bottle
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(Items.GLASS_BOTTLE),
                HTIngredientHelper.milk(250),
                HTResultHelper.item(ModItems.MILK_BOTTLE.get()),
            ).save(output)

        HTItemToObjRecipeBuilder
            .melting(
                HTIngredientHelper.item(ModItems.MILK_BOTTLE.get()),
                HTResultHelper.fluid(Tags.Fluids.MILK, 1000),
            ).saveSuffixed(output, "_from_bottle")
    }

    private fun crafting() {
        // Ragi-Cherry Popsicle
        /*HTShapedRecipeBuilder(RagiumDelightAddon.RAGI_CHERRY_POPSICLE)
            .pattern(
                " AA",
                "BAA",
                "CB ",
            ).define('A', RagiumItemTags.FOODS_RAGI_CHERRY)
            .define('B', Items.ICE)
            .define('C', Tags.Items.RODS_WOODEN)
            .save(output)*/
    }

    private fun cookingPot() {
        // Ragi-Cherry Jam
        CookingPotRecipeBuilder
            .cookingPotRecipe(
                RagiumItems.RAGI_CHERRY_JAM,
                1,
                200,
                0.35f,
                Items.GLASS_BOTTLE,
            ).addIngredient(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .addIngredient(RagiumCommonTags.Items.FOODS_RAGI_CHERRY)
            .addIngredient(Items.SUGAR)
            .setRecipeBookTab(CookingPotRecipeBookTab.MISC)
            .save(output)
    }

    private fun cutting() {
        // Ragi-Cherry Pulp
        CuttingBoardRecipeBuilder
            .cuttingRecipe(
                Ingredient.of(RagiumItems.RAGI_CHERRY),
                Ingredient.of(CommonTags.TOOLS_KNIFE),
                RagiumDelightAddon.RAGI_CHERRY_PULP,
                2,
            ).save(output)
        // Sweet Berries Cake
        CuttingBoardRecipeBuilder
            .cuttingRecipe(
                Ingredient.of(RagiumBlocks.SWEET_BERRIES_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE),
                RagiumItems.SWEET_BERRIES_CAKE_SLICE,
                7,
            ).save(output)
    }
}
