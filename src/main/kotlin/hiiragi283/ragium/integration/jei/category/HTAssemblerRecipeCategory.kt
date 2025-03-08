package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTAssemblerRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTAssemblerRecipeCategory(guiHelper: IGuiHelper) :
    HTMachineRecipeCategory<HTAssemblerRecipe>(guiHelper, HTMachineType.ASSEMBLER, 3.5) {
    override fun getRecipeType(): RecipeType<RecipeHolder<HTAssemblerRecipe>> = RagiumJEIRecipeTypes.ASSEMBLER

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTAssemblerRecipe) {
        // First Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.firstInput)
        // Second Item Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.secondInput)
        // Third Item Input
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.thirdInput.getOrNull())
        // Item Output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.output.get())
    }

    override fun getWidth(): Int = 18 * 6 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
