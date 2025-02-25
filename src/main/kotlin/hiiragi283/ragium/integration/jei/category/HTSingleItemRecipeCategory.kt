package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addItemOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder

class HTSingleItemRecipeCategory<T : HTSingleItemRecipe>(
    guiHelper: IGuiHelper,
    machine: HTMachineType,
    private val recipeType: RecipeType<RecipeHolder<T>>,
) : HTMachineRecipeCategory<T>(guiHelper, machine, 2.5) {
    override fun getRecipeType(): RecipeType<RecipeHolder<T>> = recipeType

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: T) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.input)
        // Catalyst
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.catalyst.orElse(Ingredient.of()))
        // Item Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addItemOutput(recipe.itemOutput)
    }

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
