package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.MapCodec
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.HTTripleItemToItemRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
abstract class HTTripleItemToRecipeCategory<RECIPE : HTProgressRecipe.Simple<*>>(
    guiHelper: IGuiHelper,
    recipeType: HTHolderJeiRecipeType<RECIPE>,
    codec: MapCodec<RECIPE>
) : HTHolderRecipeCategory<RECIPE>(guiHelper, recipeType, 18 * 7, 18 * 1, codec) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // input
        setPrimaryInput(
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .setSlotBackground(HTBackgroundType.INPUT),
            recipe
        )
        setSecondaryInput(
            builder
                .addInputSlot(getPosition(2), getPosition(0))
                .setSlotBackground(HTBackgroundType.EXTRA_INPUT),
            recipe
        )
        setTertiaryInput(
            builder
                .addInputSlot(getPosition(3), getPosition(0))
                .setSlotBackground(HTBackgroundType.EXTRA_INPUT),
            recipe
        )
        // output
        setOutput(
            builder
                .addOutputSlot(getPosition(6), getPosition(0))
                .setSlotBackground(HTBackgroundType.OUTPUT),
            recipe
        )
    }

    protected abstract fun setPrimaryInput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    protected abstract fun setSecondaryInput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    protected abstract fun setTertiaryInput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    protected abstract fun setOutput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(4.25), getPosition(0))
    }

    //    Basic    //

    class Basic(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTTripleItemToItemRecipe.Basic>) :
        HTTripleItemToRecipeCategory<HTTripleItemToItemRecipe.Basic>(
            guiHelper,
            recipeType,
            HTTripleItemToItemRecipe.Basic.SIMPLE_CODEC
        ) {
        override fun setPrimaryInput(builder: IRecipeSlotBuilder, recipe: HTTripleItemToItemRecipe.Basic) {
            builder.add(recipe.ingredient)
        }

        override fun setSecondaryInput(builder: IRecipeSlotBuilder, recipe: HTTripleItemToItemRecipe.Basic) {
            builder.add(recipe.secondary)
        }

        override fun setTertiaryInput(builder: IRecipeSlotBuilder, recipe: HTTripleItemToItemRecipe.Basic) {
            recipe.tertiary?.let(builder::add)
        }

        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTTripleItemToItemRecipe.Basic) {
            builder.add(recipe.result)
        }
    }
}
