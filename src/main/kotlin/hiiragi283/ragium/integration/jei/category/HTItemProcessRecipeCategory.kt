package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.jei.addFluidStack
import hiiragi283.ragium.integration.jei.addIngredients
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.ItemLike

class HTItemProcessRecipeCategory(
    guiHelper: IGuiHelper,
    private val recipeType: RecipeType<RecipeHolder<HTMachineRecipe>>,
    icon: ItemLike,
) : HTMachineRecipeCategory(guiHelper, icon, 1.5) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, definition: HTRecipeDefinition) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(definition.getItemIngredient(0))
        // Item Output
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addItemStack(definition.getItemOutput(0)?.get() ?: ItemStack.EMPTY)
        // Fluid Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addFluidStack(definition.getFluidOutput(0)?.get())
    }

    override fun getRecipeType(): RecipeType<RecipeHolder<HTMachineRecipe>> = recipeType

    override fun getWidth(): Int = 18 * 5 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
