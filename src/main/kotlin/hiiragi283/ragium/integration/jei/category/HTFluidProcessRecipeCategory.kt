package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.ItemLike

class HTFluidProcessRecipeCategory(
    guiHelper: IGuiHelper,
    private val recipeType: RecipeType<RecipeHolder<HTMachineRecipe>>,
    icon: ItemLike,
) : HTMachineRecipeCategory(guiHelper, icon, 1.5) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, definition: HTRecipeDefinition) {
        // Fluid Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(definition.getFluidIngredient(0))
        // Fluid Output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addOutput(definition.getFluidOutput(0))
    }

    override fun getRecipeType(): RecipeType<RecipeHolder<HTMachineRecipe>> = recipeType

    override fun getWidth(): Int = 18 * 4 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
