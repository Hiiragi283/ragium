package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTCrusherRecipe
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addItemOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

class HTCrusherRecipeCategory(guiHelper: IGuiHelper) : HTMachineRecipeCategory<HTCrusherRecipe>(guiHelper, HTMachineType.CRUSHER, 1.5) {
    override fun getRecipeType(): RecipeType<RecipeHolder<HTCrusherRecipe>> = RagiumJEIRecipeTypes.CRUSHER

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTCrusherRecipe) {
        // Item Input
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .setStandardSlotBackground()
            .addIngredients(recipe.input)
        // Item Outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setStandardSlotBackground()
            .addItemOutput(recipe, 0)

        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addItemOutput(recipe, 1)

        builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setStandardSlotBackground()
            .addItemOutput(recipe, 2)
    }

    override fun getWidth(): Int = 18 * 6 + 8

    override fun getHeight(): Int = 18 * 1 + 8
}
