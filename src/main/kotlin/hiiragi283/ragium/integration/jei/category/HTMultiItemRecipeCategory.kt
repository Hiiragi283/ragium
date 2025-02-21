package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.integration.jei.addIngredients
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTMultiItemRecipeCategory<T : HTMultiItemRecipe>(
    guiHelper: IGuiHelper,
    machine: HTMachineType,
    private val recipeType: RecipeType<RecipeHolder<T>>,
) : HTMachineRecipeCategory<T>(guiHelper, machine, 4.5) {
    override fun getRecipeType(): RecipeType<RecipeHolder<T>> = recipeType

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: T) {
        // First Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.itemInputs[0])
        // Second Item Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.itemInputs.getOrNull(1))
        // Third Item Input
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.itemInputs.getOrNull(2))
        // Fluid Input
        builder
            .addInputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.fluidInput.getOrNull())
        // Item Output
        builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.itemOutput.get())
    }

    override fun getWidth(): Int = 18 * 7 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
