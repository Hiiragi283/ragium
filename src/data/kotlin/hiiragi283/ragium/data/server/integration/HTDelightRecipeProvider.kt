package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTGrowthChamberRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.NeoForgeMod
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.CommonTags
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder

object HTDelightRecipeProvider : RagiumRecipeProvider.ModChild("farmersdelight") {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Sweet Berries Cake Piece
        CuttingBoardRecipeBuilder
            .cuttingRecipe(
                Ingredient.of(RagiumBlocks.SWEET_BERRIES_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE),
                RagiumItems.SWEET_BERRIES_CAKE_PIECE,
                8,
            ).save(output)
        // Yellow Cake Piece
        CuttingBoardRecipeBuilder
            .cuttingRecipe(
                Ingredient.of(RagiumItems.YELLOW_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE),
                RagiumItems.YELLOW_CAKE_PIECE,
                8,
            ).save(output)

        // Minced Beef
        CuttingBoardRecipeBuilder
            .cuttingRecipe(
                Ingredient.of(RagiumItems.MEAT_INGOT),
                Ingredient.of(CommonTags.TOOLS_KNIFE),
                ModItems.MINCED_BEEF.get(),
                2,
            ).save(output)
        // Beef Patty
        CuttingBoardRecipeBuilder
            .cuttingRecipe(
                Ingredient.of(RagiumItems.COOKED_MEAT_INGOT),
                Ingredient.of(CommonTags.TOOLS_KNIFE),
                ModItems.BEEF_PATTY.get(),
                2,
            ).save(output)

        // Rice
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(ModItems.RICE_PANICLE.get())
            .itemOutput(CommonTags.CROPS_RICE, 2)
            .save(output)

        // Milk Bottle
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Items.GLASS_BOTTLE)
            .milkInput(250)
            .itemOutput(ModItems.MILK_BOTTLE.get())
            .save(output)

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(ModItems.MILK_BOTTLE.get())
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(NeoForgeMod.MILK, 250)
            .save(output, RagiumAPI.id("milk_from_bottle"))

        // Growth
        HTGrowthChamberRecipeBuilder(lookup)
            .itemInput(ModItems.CABBAGE_SEEDS.get())
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(CommonTags.CROPS_CABBAGE, 2)
            .save(output)

        HTGrowthChamberRecipeBuilder(lookup)
            .itemInput(ModItems.TOMATO_SEEDS.get())
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(CommonTags.CROPS_TOMATO, 2)
            .save(output)

        HTGrowthChamberRecipeBuilder(lookup)
            .itemInput(ModItems.ONION.get())
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(CommonTags.CROPS_ONION, 2)
            .save(output)

        HTGrowthChamberRecipeBuilder(lookup)
            .itemInput(CommonTags.CROPS_RICE)
            .itemInput(RagiumItemTags.DIRT_SOILS)
            .itemOutput(CommonTags.CROPS_RICE, 2)
            .save(output)
    }
}
