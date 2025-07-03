package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumItemTags
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
        createSolidifying()
            .itemOutput(ModItems.MILK_BOTTLE.get())
            .itemInput(Items.GLASS_BOTTLE)
            .milkInput(250)
        // .save(output)

        createMelting()
            .fluidOutput(Tags.Fluids.MILK, 250)
            .itemInput(ModItems.MILK_BOTTLE.get())
            .saveSuffixed(output, "_from_bottle")
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
            ).addIngredient(RagiumItemTags.FOODS_RAGI_CHERRY)
            .addIngredient(RagiumItemTags.FOODS_RAGI_CHERRY)
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
