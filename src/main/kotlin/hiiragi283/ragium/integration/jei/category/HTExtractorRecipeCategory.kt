package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addFluidOutput
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addItemOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

class HTExtractorRecipeCategory(guiHelper: IGuiHelper) :
    HTMachineRecipeCategory<HTExtractorRecipe>(guiHelper, HTMachineType.EXTRACTOR, 1.5) {
    override fun getRecipeType(): RecipeType<RecipeHolder<HTExtractorRecipe>> = RagiumJEIRecipeTypes.EXTRACTOR

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTExtractorRecipe) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.input)
        // Item Output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addItemOutput(recipe, 0)
        // Fluid Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addFluidOutput(recipe, 0)
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
