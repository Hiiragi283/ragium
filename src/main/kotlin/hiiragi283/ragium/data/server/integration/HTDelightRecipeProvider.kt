package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.data.HTGrinderRecipeBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.fluids.FluidType
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
        HTGrinderRecipeBuilder()
            .itemInput(ModItems.RICE_PANICLE.get())
            .itemOutput(ModItems.RICE.get(), 2)
            .itemOutput(ModItems.STRAW.get())
            .setChance(0.5f)
            .save(output)

        // Milk Bottle
        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(Items.GLASS_BOTTLE)
            .milkInput(FluidType.BUCKET_VOLUME / 4)
            .itemOutput(ModItems.MILK_BOTTLE.get())
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(ModItems.MILK_BOTTLE.get())
            .fluidOutput(NeoForgeMod.MILK, FluidType.BUCKET_VOLUME / 4)
            .saveSuffixed(output, "_from_bottle")
    }
}
