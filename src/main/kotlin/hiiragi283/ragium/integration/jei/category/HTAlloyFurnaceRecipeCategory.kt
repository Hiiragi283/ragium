package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTAlloyFurnaceRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

class HTAlloyFurnaceRecipeCategory(guiHelper: IGuiHelper) :
    HTMachineRecipeCategory<HTAlloyFurnaceRecipe>(guiHelper, HTMachineType.ALLOY_FURNACE, 2.5) {
    override fun getRecipeType(): RecipeType<RecipeHolder<HTAlloyFurnaceRecipe>> = RagiumJEIRecipeTypes.ALLOY_FURNACE

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTAlloyFurnaceRecipe) {
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
        // Item Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.output.get())
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
