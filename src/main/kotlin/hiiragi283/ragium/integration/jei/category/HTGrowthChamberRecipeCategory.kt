package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTGrowthChamberRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addFluidStack
import hiiragi283.ragium.integration.jei.addItemOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack

class HTGrowthChamberRecipeCategory(guiHelper: IGuiHelper) :
    HTMachineRecipeCategory<HTGrowthChamberRecipe>(guiHelper, HTMachineType.GROWTH_CHAMBER, 3.5) {
    override fun getRecipeType(): RecipeType<HTGrowthChamberRecipe> = RagiumJEIRecipeTypes.GROWTH_CHAMBER

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTGrowthChamberRecipe, focuses: IFocusGroup) {
        // Seed Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.seed)
        // Soil Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.soil)
        // Water Input
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .setStandardSlotBackground()
            .apply {
                if (recipe.waterAmount > 0) {
                    addFluidStack(FluidStack(Fluids.WATER, recipe.waterAmount))
                }
            }
        // Item Output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setStandardSlotBackground()
            .addItemOutput(recipe, 0)
    }

    override fun getWidth(): Int = 18 * 6 + 8

    override fun getHeight(): Int = 18 * 1 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTGrowthChamberRecipe> =
        HTGrowthChamberRecipe.CODEC.codec()
}
