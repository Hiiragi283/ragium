package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.MapCodec
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTHolderJeiRecipeType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTBasicSingleRecipe
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
abstract class HTSingleToItemAndFluidRecipeCategory<RECIPE : HTBasicSingleRecipe<*, *, HTItemOrFluidResult>>(
    guiHelper: IGuiHelper,
    recipeType: HTHolderJeiRecipeType<RECIPE>,
    codec: MapCodec<RECIPE>
) : HTHolderRecipeCategory<RECIPE>(guiHelper, recipeType, 18 * 6, 18 * 1, codec) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // input
        setInput(
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .setSlotBackground(HTBackgroundType.INPUT),
            recipe
        )
        // outputs
        val itemOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(3), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        val fluidOutput: IRecipeSlotBuilder = builder
            .addOutputSlot(getPosition(5), getPosition(0))
            .setSlotBackground(HTBackgroundType.OUTPUT)
        recipe.result.content.mapLeft(itemOutput::add).mapRight(fluidOutput::add)
    }

    protected abstract fun setInput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addRecipeArrow(recipe).setPosition(getPosition(1.25), getPosition(0))
        builder.addRecipePlus(getPosition(4))
    }

    //    FluidTo    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.8
     */
    class FluidTo(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTFluidToRecipe.BasicItemAndFluid>) :
        HTSingleToItemAndFluidRecipeCategory<HTFluidToRecipe.BasicItemAndFluid>(
            guiHelper,
            recipeType,
            HTFluidToRecipe.BasicItemAndFluid.SIMPLE_CODEC
        ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTFluidToRecipe.BasicItemAndFluid) {
            builder.add(recipe.ingredient)
        }
    }

    //    ItemTo    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.8
     */
    class ItemTo(guiHelper: IGuiHelper, recipeType: HTHolderJeiRecipeType<HTItemToRecipe.BasicItemAndFluid>) :
        HTSingleToItemAndFluidRecipeCategory<HTItemToRecipe.BasicItemAndFluid>(
            guiHelper,
            recipeType,
            HTItemToRecipe.BasicItemAndFluid.SIMPLE_CODEC
        ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTItemToRecipe.BasicItemAndFluid) {
            builder.add(recipe.ingredient)
        }
    }
}
