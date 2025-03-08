package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTBreweryRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder
import kotlin.jvm.optionals.getOrNull

class HTBreweryRecipeCategory(guiHelper: IGuiHelper) : HTMachineRecipeCategory<HTBreweryRecipe>(guiHelper, HTMachineType.BREWERY, 4.5) {
    override fun getRecipeType(): RecipeType<RecipeHolder<HTBreweryRecipe>> = RagiumJEIRecipeTypes.BREWERY

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTBreweryRecipe) {
        // Water Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(HTBreweryRecipe.WATER_INGREDIENT)
        // First Item Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(HTBreweryRecipe.WART_INGREDIENT)
        // Second Item Input
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.firstInput)
        // Third Item Input
        builder
            .addInputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.secondInput.getOrNull())
        // Item Output
        builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(recipe.potionStack)
    }

    override fun getWidth(): Int = 18 * 7 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
