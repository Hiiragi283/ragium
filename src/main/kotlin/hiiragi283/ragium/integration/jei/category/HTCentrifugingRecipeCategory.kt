package hiiragi283.ragium.integration.jei.category

import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.integration.jei.RagiumJEIRecipeTypes
import hiiragi283.ragium.integration.jei.addIngredients
import hiiragi283.ragium.integration.jei.addOutput
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class HTCentrifugingRecipeCategory(guiHelper: IGuiHelper) : HTMachineRecipeCategory(guiHelper, Items.BARRIER, 1.5, 1.0) {
    override fun setRecipe(builder: IRecipeLayoutBuilder, definition: HTRecipeDefinition) {
        // Input
        val inputBuilder: IRecipeSlotBuilder = builder
            .addInputSlot(getPosition(0), getPosition(1))
            .setStandardSlotBackground()
        if (definition.getItemIngredient(0) != null) {
            inputBuilder.addIngredients(definition.getItemIngredient(0))
        } else if (definition.getFluidIngredient(0) != null) {
            inputBuilder.addIngredients(definition.getFluidIngredient(0))
        }
        // Item Outputs
        builder
            .addOutputSlot(getPosition(3), getPosition(1))
            .setStandardSlotBackground()
            .addOutput(definition.getItemOutput(0))

        builder
            .addOutputSlot(getPosition(4), getPosition(0))
            .setStandardSlotBackground()
            .addOutput(definition.getItemOutput(1))

        builder
            .addOutputSlot(getPosition(4), getPosition(2))
            .setStandardSlotBackground()
            .addOutput(definition.getItemOutput(2))

        builder
            .addOutputSlot(getPosition(5), getPosition(1))
            .setStandardSlotBackground()
            .addOutput(definition.getItemOutput(3))
        // Fluid Outputs
        builder
            .addOutputSlot(getPosition(6), getPosition(0))
            .setStandardSlotBackground()
            .addOutput(definition.getFluidOutput(0))

        builder
            .addOutputSlot(getPosition(6), getPosition(2))
            .setStandardSlotBackground()
            .addOutput(definition.getFluidOutput(1))
    }

    override fun getRecipeType(): RecipeType<RecipeHolder<HTMachineRecipe>> = RagiumJEIRecipeTypes.CENTRIFUGING

    override fun getWidth(): Int = 18 * 7 + 8

    override fun getHeight(): Int = 18 * 3 + 8
}
