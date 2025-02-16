package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRefineryRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addFluidOutput
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addItemOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType

class HTRefineryRecipeCategory(guiHelper: IGuiHelper) :
    HTMachineRecipeCategory<HTRefineryRecipe>(guiHelper, HTMachineType.REFINERY, 1.5, 2.0) {
    override fun getRecipeType(): RecipeType<HTRefineryRecipe> = RagiumJEIRecipeTypes.REFINERY

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTRefineryRecipe, focuses: IFocusGroup) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(2))
            .setStandardSlotBackground()
            .addIngredients(recipe.input)
        // Item Output
        builder
            .addOutputSlot(getPosition(3), getPosition(2))
            .setStandardSlotBackground()
            .addItemOutput(recipe, 0)
        // Fluid Output
        builder
            .addOutputSlot(getPosition(4), getPosition(2))
            .setStandardSlotBackground()
            .addFluidOutput(recipe, 0)

        builder
            .addOutputSlot(getPosition(4), getPosition(1))
            .setStandardSlotBackground()
            .addFluidOutput(recipe, 1)

        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addFluidOutput(recipe, 2)
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 3 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTRefineryRecipe> =
        HTRefineryRecipe.CODEC.codec()
}
