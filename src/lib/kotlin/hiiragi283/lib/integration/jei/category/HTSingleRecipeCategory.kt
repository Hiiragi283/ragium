package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.MapCodec
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
abstract class HTSingleRecipeCategory<RECIPE : HTProgressRecipe.Simple<*>>(
    guiHelper: IGuiHelper,
    recipeType: HTHolderJeiRecipeType<RECIPE>,
    codec: MapCodec<RECIPE>
) : HTHolderRecipeCategory<RECIPE>(guiHelper, recipeType, 18 * 4, 18 * 1, codec) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // input
        setInput(
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .setSlotBackground(HTBackgroundType.INPUT),
            recipe
        )
        // output
        setOutput(
            builder
                .addInputSlot(getPosition(3), getPosition(0))
                .setSlotBackground(HTBackgroundType.OUTPUT),
            recipe
        )
    }

    protected abstract fun setInput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    protected abstract fun setOutput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
    }

    //    FluidToFluid    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.6
     */
    class FluidToFluid(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTFluidToRecipe.BasicFluid>) :
        HTSingleRecipeCategory<HTFluidToRecipe.BasicFluid>(
            guiHelper,
            recipeType,
            HTFluidToRecipe.BasicFluid.SIMPLE_CODEC
        ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTFluidToRecipe.BasicFluid) {
            val ingredient: HTFluidIngredient = recipe.ingredient
            builder.add(ingredient).setFluidSlot(ingredient.amount)
        }

        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTFluidToRecipe.BasicFluid) {
            val result: HTFluidResult = recipe.result
            builder.add(result).setFluidSlot(result.amount)
        }
    }

    //    FluidToItem    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.6
     */
    class FluidToItem(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTFluidToRecipe.BasicItem>) :
        HTSingleRecipeCategory<HTFluidToRecipe.BasicItem>(
            guiHelper,
            recipeType,
            HTFluidToRecipe.BasicItem.SIMPLE_CODEC
        ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTFluidToRecipe.BasicItem) {
            val ingredient: HTFluidIngredient = recipe.ingredient
            builder.add(ingredient).setFluidSlot(ingredient.amount)
        }

        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTFluidToRecipe.BasicItem) {
            builder.add(recipe.result)
        }
    }

    //    ItemToFluid    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.6
     */
    class ItemToFluid(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTItemToRecipe.BasicFluid>) :
        HTSingleRecipeCategory<HTItemToRecipe.BasicFluid>(
            guiHelper,
            recipeType,
            HTItemToRecipe.BasicFluid.SIMPLE_CODEC
        ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTItemToRecipe.BasicFluid) {
            builder.add(recipe.ingredient)
        }

        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTItemToRecipe.BasicFluid) {
            val result: HTFluidResult = recipe.result
            builder.add(result).setFluidSlot(result.amount)
        }
    }

    //    ItemToItem    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.6
     */
    class ItemToItem(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTItemToRecipe.BasicItem>) :
        HTSingleRecipeCategory<HTItemToRecipe.BasicItem>(guiHelper, recipeType, HTItemToRecipe.BasicItem.SIMPLE_CODEC) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTItemToRecipe.BasicItem) {
            builder.add(recipe.ingredient)
        }

        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTItemToRecipe.BasicItem) {
            builder.add(recipe.result)
        }
    }
}
