package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.MapCodec
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTBasicSingleRecipe
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeType

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
abstract class HTSingleRecipeCategory<RECIPE : HTBasicSingleRecipe<*, *, *>>(
    recipeType: HTRecipeHolderType<RECIPE>,
    recipeType1: RecipeType<*>,
    icon: IDrawable,
    codec: MapCodec<RECIPE>
) : HTHolderRecipeCategory<RECIPE>(recipeType, recipeType1, icon, 18 * 4, 18 * 1, codec) {
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
                .addOutputSlot(getPosition(3), getPosition(0))
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
    class FluidToFluid(
        recipeType: HTRecipeHolderType<HTFluidToRecipe.BasicFluid>,
        recipeType1: RecipeType<*>,
        icon: IDrawable
    ) : HTSingleRecipeCategory<HTFluidToRecipe.BasicFluid>(
        recipeType,
        recipeType1,
        icon,
        HTFluidToRecipe.BasicFluid.SIMPLE_CODEC
    ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTFluidToRecipe.BasicFluid) {
            builder.add(recipe.ingredient)
        }

        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTFluidToRecipe.BasicFluid) {
            builder.add(recipe.result)
        }
    }

    //    FluidToItem    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.6
     */
    class FluidToItem(
        recipeType: HTRecipeHolderType<HTFluidToRecipe.BasicItem>,
        recipeType1: RecipeType<*>,
        icon: IDrawable
    ) : HTSingleRecipeCategory<HTFluidToRecipe.BasicItem>(
        recipeType,
        recipeType1,
        icon,
        HTFluidToRecipe.BasicItem.SIMPLE_CODEC
    ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTFluidToRecipe.BasicItem) {
            builder.add(recipe.ingredient)
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
    class ItemToFluid(
        recipeType: HTRecipeHolderType<HTItemToRecipe.BasicFluid>,
        recipeType1: RecipeType<*>,
        icon: IDrawable
    ) : HTSingleRecipeCategory<HTItemToRecipe.BasicFluid>(
        recipeType,
        recipeType1,
        icon,
        HTItemToRecipe.BasicFluid.SIMPLE_CODEC
    ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTItemToRecipe.BasicFluid) {
            builder.add(recipe.ingredient)
        }

        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTItemToRecipe.BasicFluid) {
            builder.add(recipe.result)
        }
    }

    //    ItemToItem    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.6
     */
    class ItemToItem(
        recipeType: HTRecipeHolderType<HTItemToRecipe.BasicItem>,
        recipeType1: RecipeType<*>,
        icon: IDrawable
    ) : HTSingleRecipeCategory<HTItemToRecipe.BasicItem>(
        recipeType,
        recipeType1,
        icon,
        HTItemToRecipe.BasicItem.SIMPLE_CODEC
    ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTItemToRecipe.BasicItem) {
            builder.add(recipe.ingredient)
        }

        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTItemToRecipe.BasicItem) {
            builder.add(recipe.result)
        }
    }
}
