package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class HTInfusingRecipeCategory(guiHelper: IGuiHelper) : HTMachineRecipeCategory(guiHelper, Items.BARRIER, 2.5) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, definition: HTRecipeDefinition) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(definition.getItemIngredient(0))
        // Fluid Input
        builder
            .addInputSlot(getPosition(1), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(definition.getFluidIngredient(0))
        // Item Output
        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addOutput(definition.getItemOutput(0))
        // Fluid Output
        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setStandardSlotBackground()
            .addOutput(definition.getFluidOutput(0))
    }

    override fun getRecipeType(): RecipeType<RecipeHolder<HTMachineRecipe>> = RagiumJEIRecipeTypes.INFUSING

    override fun getWidth(): Int = 18 * 6 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
