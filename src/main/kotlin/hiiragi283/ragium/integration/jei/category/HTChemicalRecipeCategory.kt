package hiiragi283.ragium.integration.jei.category

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.recipe.HTChemicalRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeCondition
import hiiragi283.ragium.integration.jei.addFluidStack
import hiiragi283.ragium.integration.jei.addIngredients
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.placement.HorizontalAlignment
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.ICodecHelper
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.IRecipeManager
import mezz.jei.api.recipe.RecipeType
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class HTChemicalRecipeCategory(val guiHelper: IGuiHelper) : HTRecipeCategory<HTChemicalRecipe> {
    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: HTChemicalRecipe, focuses: IFocusGroup) {
        // inputs
        addItemInput(builder, recipe, 0, 0, 0)
        addItemInput(builder, recipe, 1, 0, 1)
        addItemInput(builder, recipe, 2, 0, 2)
        addFluidInput(builder, recipe, 0, 1, 0)
        addFluidInput(builder, recipe, 1, 1, 1)
        addFluidInput(builder, recipe, 2, 1, 2)
        // catalyst
        addCatalyst(builder, recipe, 1)
        // outputs
        addItemOutput(builder, recipe, 5, 0, 0)
        addItemOutput(builder, recipe, 6, 0, 1)
        addItemOutput(builder, recipe, 7, 0, 2)
        addFluidOutput(builder, recipe, 5, 1, 0)
        addFluidOutput(builder, recipe, 6, 1, 1)
        addFluidOutput(builder, recipe, 7, 1, 2)
    }

    override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: HTChemicalRecipe, focuses: IFocusGroup) {
        // Recipe Arrow
        builder.addRecipeArrow().setPosition(getPosition(3.5), getPosition(0))
        // Condition Info
        recipe.condition.ifPresent { condition: HTMachineRecipeCondition ->
            builder
                .addText(condition.text, width - 4, 10)
                .setPosition(getPosition(0), getPosition(2))
                .setShadow(true)
                .setTextAlignment(HorizontalAlignment.LEFT)
        }
    }

    private fun addItemInput(
        builder: IRecipeLayoutBuilder,
        recipe: HTChemicalRecipe,
        x: Int,
        y: Int,
        index: Int,
    ) {
        builder
            .addInputSlot(getPosition(x), getPosition(y))
            .setStandardSlotBackground()
            .addIngredients(recipe.itemInputs.getOrNull(index))
    }

    private fun addCatalyst(builder: IRecipeLayoutBuilder, recipe: HTChemicalRecipe, y: Int) {
        val slotBuilder: IRecipeSlotBuilder = builder
            .addInputSlot(5 + 9 + 3 * 18, getPosition(y))
            .setStandardSlotBackground()

        recipe.condition.ifPresent { condition: HTMachineRecipeCondition ->
            when (condition) {
                is HTMachineRecipeCondition.ItemBased ->
                    slotBuilder.addIngredients(condition.itemIngredient)

                is HTMachineRecipeCondition.FluidBased ->
                    slotBuilder.addIngredients(condition.fluidIngredient)

                else -> {}
            }
        }
    }

    private fun addFluidInput(
        builder: IRecipeLayoutBuilder,
        recipe: HTChemicalRecipe,
        x: Int,
        y: Int,
        index: Int,
    ) {
        builder
            .addInputSlot(getPosition(x), getPosition(y))
            .setStandardSlotBackground()
            .addIngredients(recipe.fluidInputs.getOrNull(index))
    }

    private fun addItemOutput(
        builder: IRecipeLayoutBuilder,
        recipe: HTChemicalRecipe,
        x: Int,
        y: Int,
        index: Int,
    ) {
        builder
            .addOutputSlot(getPosition(x), getPosition(y))
            .setStandardSlotBackground()
            .addItemStack(recipe.getItemOutputs().getOrNull(index) ?: ItemStack.EMPTY)
    }

    private fun addFluidOutput(
        builder: IRecipeLayoutBuilder,
        recipe: HTChemicalRecipe,
        x: Int,
        y: Int,
        index: Int,
    ) {
        builder
            .addOutputSlot(getPosition(x), getPosition(y))
            .setStandardSlotBackground()
            .addFluidStack(recipe.getFluidOutputs().getOrNull(index))
    }

    //    IRecipeCategory    //

    override fun getRecipeType(): RecipeType<HTChemicalRecipe> = TODO()

    override fun getTitle(): Component = TODO()

    override fun getIcon(): IDrawable? = TODO()

    override fun getWidth(): Int = 18 * 8 + 8

    override fun getHeight(): Int = 18 * 2 + 8

    override fun getCodec(codecHelper: ICodecHelper, recipeManager: IRecipeManager): Codec<HTChemicalRecipe> =
        HTChemicalRecipe.CODEC.codec()
}
