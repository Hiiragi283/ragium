package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.MapCodec
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeType

abstract class HTItemAndFluidToRecipeCategory<RECIPE : HTItemAndFluidToRecipe.Basic<*, *>>(
    recipeType: HTRecipeHolderType<RECIPE>,
    recipeType1: RecipeType<*>,
    icon: IDrawable,
    codec: MapCodec<RECIPE>
) : HTHolderRecipeCategory<RECIPE>(
    recipeType,
    recipeType1,
    icon,
    18 * 6,
    18 * 1,
    codec
) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // inputs
        builder
            .addInputSlot(getPosition(0), getPosition(0))
            .add(recipe.fluidIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.itemIngredient)
            .setSlotBackground(HTBackgroundType.INPUT)
        // output
        setOutput(
            builder
                .addOutputSlot(getPosition(5), getPosition(0))
                .setSlotBackground(HTBackgroundType.OUTPUT),
            recipe
        )
    }

    protected abstract fun setOutput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }

    //    ToFluid    //

    class ToFluid(
        recipeType: HTRecipeHolderType<HTItemAndFluidToRecipe.BasicFluid>,
        recipeType1: RecipeType<*>,
        icon: IDrawable
    ) : HTItemAndFluidToRecipeCategory<HTItemAndFluidToRecipe.BasicFluid>(
        recipeType,
        recipeType1,
        icon,
        HTItemAndFluidToRecipe.BasicFluid.SIMPLE_CODEC
    ) {
        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTItemAndFluidToRecipe.BasicFluid) {
            builder.add(recipe.result)
        }
    }

    //    ToItem    //

    class ToItem(
        recipeType: HTRecipeHolderType<HTItemAndFluidToRecipe.BasicItem>,
        recipeType1: RecipeType<*>,
        icon: IDrawable
    ) : HTItemAndFluidToRecipeCategory<HTItemAndFluidToRecipe.BasicItem>(
        recipeType,
        recipeType1,
        icon,
        HTItemAndFluidToRecipe.BasicItem.SIMPLE_CODEC
    ) {
        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTItemAndFluidToRecipe.BasicItem) {
            builder.add(recipe.result)
        }
    }
}
