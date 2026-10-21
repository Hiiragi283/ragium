package hiiragi283.lib.integration.jei.category

import com.mojang.serialization.MapCodec
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.integration.jei.HTRecipeHolderType
import hiiragi283.lib.integration.jei.add
import hiiragi283.lib.recipe.base.HTCatalysedSingleRecipe
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.recipe.IFocusGroup
import net.minecraft.world.item.crafting.RecipeType

abstract class HTCatalysedSingleRecipeCategory<RECIPE : HTCatalysedSingleRecipe<*, *>>(
    recipeType: HTRecipeHolderType<RECIPE>,
    recipeType1: RecipeType<*>,
    icon: IDrawable,
    codec: MapCodec<RECIPE>
) : HTHolderRecipeCategory<RECIPE>(recipeType, recipeType1, icon, 18 * 6, 18 * 1, codec) {
    override fun setupRecipe(builder: IRecipeLayoutBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        // input
        setInput(
            builder
                .addInputSlot(getPosition(0), getPosition(0))
                .setSlotBackground(HTBackgroundType.INPUT),
            recipe
        )
        // catalyst
        builder
            .addInputSlot(getPosition(2), getPosition(0))
            .add(recipe.catalyst)
            .setSlotBackground(HTBackgroundType.NONE)
        // output
        setOutput(
            builder
                .addOutputSlot(getPosition(5), getPosition(0))
                .setSlotBackground(HTBackgroundType.OUTPUT),
            recipe
        )
    }

    protected abstract fun setInput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    protected abstract fun setOutput(builder: IRecipeSlotBuilder, recipe: RECIPE)

    override fun setupRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RECIPE, focuses: IFocusGroup) {
        builder.addRecipePlus(getPosition(1))
        builder.addRecipeArrow(recipe).setPosition(getPosition(3.25), getPosition(0))
    }

    class ItemToItem(
        recipeType: HTRecipeHolderType<HTDoubleItemToItemRecipe.Catalysed>,
        recipeType1: RecipeType<*>,
        icon: IDrawable
    ) : HTCatalysedSingleRecipeCategory<HTDoubleItemToItemRecipe.Catalysed>(
        recipeType,
        recipeType1,
        icon,
        HTDoubleItemToItemRecipe.Catalysed.SIMPLE_CODEC
    ) {
        override fun setInput(builder: IRecipeSlotBuilder, recipe: HTDoubleItemToItemRecipe.Catalysed) {
            builder.add(recipe.ingredient)
        }

        override fun setOutput(builder: IRecipeSlotBuilder, recipe: HTDoubleItemToItemRecipe.Catalysed) {
            builder.add(recipe.result)
        }
    }
}
